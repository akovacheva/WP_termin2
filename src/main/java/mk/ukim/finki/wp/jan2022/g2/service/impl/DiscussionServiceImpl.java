package mk.ukim.finki.wp.jan2022.g2.service.impl;

import mk.ukim.finki.wp.jan2022.g2.model.Discussion;
import mk.ukim.finki.wp.jan2022.g2.model.DiscussionTag;
import mk.ukim.finki.wp.jan2022.g2.model.User;
import mk.ukim.finki.wp.jan2022.g2.model.exceptions.InvalidDiscussionIdException;
import mk.ukim.finki.wp.jan2022.g2.model.exceptions.InvalidUserIdException;
import mk.ukim.finki.wp.jan2022.g2.repository.DiscussionRepository;
import mk.ukim.finki.wp.jan2022.g2.repository.UserRepository;
import mk.ukim.finki.wp.jan2022.g2.service.DiscussionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DiscussionServiceImpl implements DiscussionService
{
    private final DiscussionRepository discussionRepository;
    private final UserRepository userRepository;

    public DiscussionServiceImpl(DiscussionRepository discussionRepository, UserRepository userRepository) {
        this.discussionRepository = discussionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Discussion> listAll() {
        return this.discussionRepository.findAll();
    }

    @Override
    public Discussion findById(Long id) {
        return this.discussionRepository.findById(id).orElseThrow(InvalidDiscussionIdException::new);
    }

    @Override
    public Discussion create(String title, String description, DiscussionTag discussionTag, List<Long> participantsId, LocalDate dueDate) {
        List<User> participants = this.userRepository.findAllById(participantsId);
        Discussion discussion = new Discussion(title, description, discussionTag, participants, dueDate);
        return this.discussionRepository.save(discussion);
    }

    @Override
    public Discussion update(Long id, String title, String description, DiscussionTag discussionTag, List<Long> participantsId) {
        Discussion discussion = this.discussionRepository.findById(id).orElseThrow(InvalidDiscussionIdException::new);
        discussion.setTitle(title);
        discussion.setDescription(description);
        discussion.setTag(discussionTag);
        List<User> participants = this.userRepository.findAllById(participantsId);
        discussion.setParticipants(participants);
        return this.discussionRepository.save(discussion);
    }

    @Override
    public Discussion delete(Long id) {
        Discussion discussion = this.discussionRepository.findById(id).orElseThrow(InvalidDiscussionIdException::new);
        discussionRepository.delete(discussion);
        return discussion;
    }

    @Override
    public Discussion markPopular(Long id) {
        Discussion discussion = this.discussionRepository.findById(id).orElseThrow(InvalidDiscussionIdException::new);
        discussion.setPopular(true);
        return this.discussionRepository.save(discussion);
    }

    @Override
    public List<Discussion> filter(Long participantId, Integer daysUntilClosing) {
        if (participantId != null) {
            User participant = this.userRepository.findById(participantId).orElseThrow(InvalidUserIdException::new);

            if (daysUntilClosing != null) {
                return this.discussionRepository.findAllByParticipantsContainsAndDueDateBefore(participant, LocalDate.now().plusDays(daysUntilClosing));
            } else {
                return this.discussionRepository.findAllByParticipantsContains(participant);
            }
        } else if (daysUntilClosing != null) {
            return this.discussionRepository.findAllByDueDateBefore(LocalDate.now().plusDays(daysUntilClosing));
        } else {
            return this.discussionRepository.findAll();
        }
    }

}
