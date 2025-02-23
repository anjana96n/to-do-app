import { Component, OnInit ,NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss'],
})
export class TaskListComponent implements OnInit {
  tasks: Task[] = [];
  titleError: string = '';

  task = {
    title: '',
    description: '',
    dueDate: '',
    priority: 'MEDIUM' as 'LOW' | 'MEDIUM' | 'HIGH', // Default to 'MEDIUM'
  };

  constructor(private taskService: TaskService) {}

  async ngOnInit() {
    this.tasks = await this.taskService.getTasks();
  }

  async addTask(taskForm: any) {
    const { title, description, dueDate, priority } = taskForm.value;
  
    // Check if title is empty and set error message
    if (!title) {
      this.titleError = 'Title should not be empty';
      return;
    }
  
    // Check if all fields are filled out
    if (!title || !description || !dueDate || !priority) {
      this.titleError = 'All fields are required';
      return;
    }
  
    // Clear title error if all fields are valid
    this.titleError = '';
  
    const newTask: Task = {
      title,
      description,
      dueDate,
      priority: priority as 'LOW' | 'MEDIUM' | 'HIGH',
    };
  
    try {
      // Add task using the service
      const addedTask = await this.taskService.addTask(newTask);
      this.tasks.unshift(addedTask);
  
      // Reset the form fields after successful task addition
      taskForm.reset();
      
      // Reset priority to 'MEDIUM' after form reset
      taskForm.form.controls['priority'].setValue('MEDIUM');
  
    } catch (error) {
      // Handle any errors from taskService
      console.error('Error adding task:', error);
    }
  
    // Clear error message after successful task addition
    this.titleError = '';
  }
  

  async completeTask(id: number) {
    await this.taskService.completeTask(id);
    this.tasks = this.tasks.filter(task => task.id !== id);
  }

  trackTask(index: number, task: any): number {
    return task.id;  
  }
  
}
