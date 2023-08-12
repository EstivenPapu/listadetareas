
package com.aprendiendospring.app.controlador;

import com.aprendiendospring.app.model.Task;
import com.aprendiendospring.app.model.TaskRepository;
import java.time.LocalDate;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
@Controller
public class TaskController {

     @Autowired
    private TaskRepository taskRepository;
      
     
    @GetMapping("/")
    public String listTasks(Model model) {
        List<Task> tasks = taskRepository.findAll();
        model.addAttribute("tasks", tasks);
        return "Task/list"; 
    }
     
    //para añadir
   @PostMapping("/add")
public String addTask(@RequestParam String taskDescription, @RequestParam(required = false) LocalDate dueDate) {
    if (taskDescription != null && !taskDescription.trim().isEmpty()) {
        Task newTask = new Task();
        
      
        newTask.setDueDate(dueDate);
        
        newTask.setDescription(taskDescription);
        taskRepository.save(newTask);
    }
    return "redirect:/";
}

// Marcar tarea como completada
@PostMapping("/complete/{id}")
public String completeTask(@PathVariable Long id) {
    Task task = taskRepository.findById(id).orElse(null);
    if (task != null) {
        task.setCompleted(true);
        taskRepository.save(task);
    }
    return "redirect:/";
}

// Establecer fecha de vencimiento
@PostMapping("/setDueDate/{id}")
public String setDueDate(@PathVariable Long id, @RequestParam LocalDate dueDate) {
    Task task = taskRepository.findById(id).orElse(null);
    if (task != null) {
        task.setDueDate(dueDate);
        taskRepository.save(task);
    }
    return "redirect:/";
}

    //para editar
    @GetMapping("/edit/{id}")
    public String editTask(@PathVariable long id, Model model) {
                List<Task> tasks = taskRepository.findAll();

    Task taskToEdit = null;
        for (Task task : tasks) {
        if (task.getId() == id) {
            taskToEdit = task;
            break;
        }
        }

        if (taskToEdit == null) {
            throw new IllegalArgumentException("Tarea no encontrada: " + id);
        }
    
        model.addAttribute("task", taskToEdit);
        return "Task/edit";
    }
    
    //para actualizar
    @PostMapping("/update")
    public String updateTask(@ModelAttribute Task task) {
    Task existingTask = taskRepository.findById(task.getId()).orElse(null);
    if (existingTask != null) {
        existingTask.setCompleted(task.isCompleted());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setDescription(task.getDescription());
        taskRepository.save(existingTask);
    }
    return "redirect:/";
    }
    
    //para borrar
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
       Task taskToDelete = taskRepository.findById(id).orElse(null);
    if (taskToDelete != null) {
        taskRepository.delete(taskToDelete);
    }
    return "redirect:/";
    }
    
}
