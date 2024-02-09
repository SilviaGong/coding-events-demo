package org.launchcode.codingevents.controllers;

import org.launchcode.codingevents.data.TagRepository;
import org.launchcode.codingevents.models.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * This class serves as the controller for Tag-related operations.
 * Handles requests related to tags.
 */
@Controller
@RequestMapping("tags")
public class TagController {

    // Inject TagRepository dependency
    @Autowired
    TagRepository tagRepository;

    // Displays all tags
    @GetMapping
    public String displayAllTags(Model model) {
        model.addAttribute("title", "All Tags");
        model.addAttribute("tags", tagRepository.findAll());
        return "tags/index"; // Return the view for displaying all tags
    }

    // Renders the form to create a new tag
    @GetMapping("create")
    public String renderCreateTagForm(Model model) {
        model.addAttribute("title", "Create Tag");
        model.addAttribute(new Tag()); // Add an empty Tag object to the model
        return "tags/create"; // Return the view for creating a new tag
    }

    // Processes the submission of the create tag form
    @PostMapping("create")
    public String processCreateTagForm(@Valid @ModelAttribute Tag tag,
                                       Errors errors, Model model) {

        // Check for validation errors
        if (errors.hasErrors()) {
            model.addAttribute("title", "Create Tag");
            model.addAttribute(new Tag()); // Add an empty Tag object to the model
            return "tags/create"; // Return the view for creating a new tag
        }

        // Save the new tag
        tagRepository.save(tag);
        return "redirect:/tags"; // Redirect to the URL for displaying all tags
    }
}
