package mk.ukim.finki.wp.jan2022.g2.web;

import mk.ukim.finki.wp.jan2022.g2.model.Discussion;
import mk.ukim.finki.wp.jan2022.g2.model.DiscussionTag;
import mk.ukim.finki.wp.jan2022.g2.model.User;
import mk.ukim.finki.wp.jan2022.g2.service.DiscussionService;
import mk.ukim.finki.wp.jan2022.g2.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class DiscussionController {

    private final DiscussionService service;
    private final UserService userService;

    public DiscussionController(DiscussionService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    /**
     * This method should use the "list.html" template to display all entities.
     * The method should be mapped on paths '/' and '/discussions'.
     * The arguments that this method takes are optional and can be 'null'.
     *
     * @return The view "list.html".
     */
    @GetMapping({"/","/discussions"})
    public String showList(Long participantId, Integer daysUntilClosing, Model model)
    {
        List<Discussion> discussions;
        List<User> participants = this.userService.listAll();
        if (participantId == null && daysUntilClosing == null) {
            discussions = this.service.listAll();
        } else {
            discussions = this.service.filter(participantId, daysUntilClosing);
        }
        model.addAttribute("discussions", discussions);
        model.addAttribute("participants", participants);
        return "list.html";
    }

    /**
     * This method should display the "form.html" template.
     * The method should be mapped on path '/discussions/add'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/discussions/add")
    public String showAdd(Model model)
    {
        List<User> participants = this.userService.listAll();
        DiscussionTag[] discussionTag = DiscussionTag.values();
        model.addAttribute("participants", participants);
        model.addAttribute("tags", discussionTag);

        return "form.html";
    }

    /**
     * This method should display the "form.html" template.
     * However, in this case all 'input' elements should be filled with the appropriate value for the entity that is updated.
     * The method should be mapped on path '/discussions/[id]/edit'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/discussions/{id}/edit")
    public String showEdit(@PathVariable Long id, Model model) {
        Discussion discussion = this.service.findById(id);
        List<User> participants = this.userService.listAll();
        DiscussionTag[] discussionTag = DiscussionTag.values();
        model.addAttribute("discussion", discussion);
        model.addAttribute("participants", participants);
        model.addAttribute("tags", discussionTag);
        return "form.html";
    }
    /**
     * This method should create an entity given the arguments it takes.
     * The method should be mapped on path '/discussions'.
     * After the entity is created, the list of entities should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/discussions")
    public String create(@RequestParam String title,
                         @RequestParam String description,
                         @RequestParam DiscussionTag discussionTag,
                         @RequestParam List<Long> participants,
                         @RequestParam @DateTimeFormat (iso = DateTimeFormat.ISO.DATE) String dueDate)
    {
        this.service.create(title, description, discussionTag, participants, LocalDate.parse(dueDate));
        return "redirect:/discussions";
    }

    /**
     * This method should update an entity given the arguments it takes.
     * The method should be mapped on path '/discussions/[id]'.
     * After the entity is updated, the list of entities should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/discussions/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String title,
                         @RequestParam String description,
                         @RequestParam DiscussionTag discussionTag,
                         @RequestParam List<Long> participants) {
        this.service.update(id, title, description, discussionTag, participants);
        return "redirect:/discussions";
    }

    /**
     * This method should delete the entity that has the appropriate identifier.
     * The method should be mapped on path '/discussions/[id]/delete'.
     * After the entity is deleted, the list of entities should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/discussions/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.service.delete(id);
        return "redirect:/discussions";
    }

    /**
     * This method should mark the discussion that has the appropriate identifier as popular.
     * The method should be mapped on path '/discussions/[id]/mark_popular'.
     * After its execution, the list of entities should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/discussions/{id}/mark_popular")
    public String markPopular(@PathVariable Long id)
    {
        this.service.markPopular(id);
        return "redirect:/discussions";
    }
}
