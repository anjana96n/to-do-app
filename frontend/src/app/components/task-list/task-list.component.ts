import { Component, OnInit } from '@angular/core';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent implements OnInit {
  tasks: Task[] = [];

  constructor(private taskService: TaskService) {}

  async ngOnInit() {
    this.tasks = await this.taskService.getTasks();
  }

  async addTask(title: string, description: string, dueDate: string, priority: string) {
    const newTask: Task = {
      title,
      description,
      dueDate,
      priority: priority as 'LOW' | 'MEDIUM' | 'HIGH' // Cast priority to a valid type
    };
    
    const addedTask = await this.taskService.addTask(newTask);
    this.tasks.unshift(addedTask);
  }
  

  async completeTask(id: number) {
    await this.taskService.completeTask(id);
    this.tasks = this.tasks.filter(task => task.id !== id);
  }
}
