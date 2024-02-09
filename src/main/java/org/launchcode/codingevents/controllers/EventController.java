package org.launchcode.codingevents.controllers;

import org.launchcode.codingevents.data.EventCategoryRepository;
import org.launchcode.codingevents.data.EventRepository;
import org.launchcode.codingevents.data.TagRepository;
import org.launchcode.codingevents.models.Event;
import org.launchcode.codingevents.models.EventCategory;
import org.launchcode.codingevents.models.Tag;
import org.launchcode.codingevents.models.dto.EventTagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * This class handles HTTP requests related to events.
 * Created by Chris Bay
 */
@Controller
@RequestMapping("events")
public class EventController {

    // Autowire repositories
    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventCategoryRepository eventCategoryRepository;

    @Autowired
    TagRepository tagRepository;

    // Handle request to display all events
    @GetMapping()
    public String displayAllEvent(@RequestParam(required = false) Integer categoryId, Model model) {// the parameter categoryId is optional(required =false)
        // If no category is specified, display all events
        if (categoryId == null) {
            model.addAttribute("title", "All Events");
            model.addAttribute("events", eventRepository.findAll());
        } else {
            // If category is specified, display events in that category
            Optional<EventCategory> result = eventCategoryRepository.findById(categoryId);
            if (result.isEmpty()) {
                model.addAttribute("title", "Invalid Category ID :" + categoryId);
            } else {
                EventCategory eventCategory = result.get();
                model.addAttribute("title", "Event in category: " + eventCategory.getName());
                model.addAttribute("events", eventCategory.getEvents());
            }
        }
        return "events/index";
    }

    // Handle request to display form to create a new event
    @GetMapping("create")
    public String displayCreateEventForm(Model model) {
        model.addAttribute("title", "Create Event");
        model.addAttribute(new Event()); // Add an empty Event object to the model
        model.addAttribute("categories", eventCategoryRepository.findAll()); // Add all event categories to the model
        return "events/create";
    }

    // Handle submission of form to create a new event
    @PostMapping("create")
    public String processCreateEventForm(@ModelAttribute @Valid Event newEvent, Errors errors, Model model) {
        // Check for validation errors
        if (errors.hasErrors()) {
            model.addAttribute("title", "Create Event");
            return "events/create"; // Return to the create event form if there are errors
        }
        eventRepository.save(newEvent); // Save the new event to the database
        return "redirect:/events"; // Redirect to the list of all events
    }

    // Handle request to display form to delete events
    @GetMapping("delete")
    public String displayDeleteEventForm(Model model) {
        model.addAttribute("title", "Delete Events");
        model.addAttribute("events", eventRepository.findAll()); // Add all events to the model
        return "events/delete";
    }

    // Handle submission of form to delete events
    @PostMapping("delete")
    public String processDeleteEventsForm(@RequestParam(required = false) int[] eventIds) {
        // Check if any events were selected for deletion
        if (eventIds != null) {
            // Loop through the selected event IDs and delete each event
            for (int id : eventIds) {
                eventRepository.deleteById(id);
            }
        }
        return "redirect:/events"; // Redirect to the list of all events
    }

    // Handle request to display details of a specific event
    @GetMapping("detail")
    public String displayEventDetails(@RequestParam Integer eventId, Model model) {
        // Find the event by its ID
        Optional<Event> result = eventRepository.findById(eventId);
        // Check if the event exists
        if (result.isEmpty()) {
            model.addAttribute("title", "Invalid Event Id: " + eventId);
        } else {
            // Add the event details to the model
            Event event = result.get();
            model.addAttribute("title", event.getName() + " Detail");
            model.addAttribute("event", event);
        }
        return "events/detail";
    }

    // Handle request to display form to add a tag to an event
    @GetMapping("add-tag")
    public String displayAddTagForm(@RequestParam Integer eventId, Model model) {
        // Find the event by its ID
        Optional<Event> result = eventRepository.findById(eventId);
        // Get the event object from the optional result
        Event event = result.get();
        model.addAttribute("title","Add Tag to:"+event.getName()); // Add title to the model
        model.addAttribute("tags",tagRepository.findAll()); // Add all tags to the model
        model.addAttribute("event",event); // Add event to the model
        model.addAttribute("eventTag",new EventTagDTO()); // Add a new EventTagDTO object to the model
        return "events/add-tag.html"; // Return the add tag form
    }

    // Handle submission of form to add a tag to an event
    @PostMapping("add-tag")
    public String processAddTagForm(@ModelAttribute @Valid EventTagDTO eventTag,Errors errors,Model model){
        // Check for validation errors
        if(!errors.hasErrors()){
            // Get the event and tag objects from the form
            Event event=eventTag.getEvent();
            Tag tag=eventTag.getTag();
            // Check if the event already contains the tag
            if(!event.getTags().contains(tag)){
                // If not, add the tag to the event
                event.addTag(tag);
                eventRepository.save(event); // Save the event with the new tag
            }
            return "redirect:detail?eventId="+event.getId(); // Redirect to the event details page
        }
        return "redirect:add-tag"; // Redirect to the add tag form if there are errors
    }
}
