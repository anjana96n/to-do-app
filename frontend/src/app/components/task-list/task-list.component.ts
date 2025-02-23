import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './task-list.component.html',
  styles: [`
    .task-container {
      display: flex;
      gap: 2rem;
      padding: 2rem;
      max-width: 1200px;
      margin: 0 auto;
    }

    .add-task-section, .task-list-section {
      flex: 1;
      background: white;
      border-radius: 8px;
      padding: 1.5rem;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }

    h2 {
      color: #333;
      margin-bottom: 1.5rem;
    }

    .task-form {
      display: flex;
      flex-direction: column;
      gap: 1rem;
    }

    .form-input {
      padding: 0.75rem;
      border: 1px solid #ddd;
      border-radius: 4px;
      font-size: 1rem;
    }

    textarea.form-input {
      min-height: 100px;
      resize: vertical;
    }

    .add-button {
      background: #4CAF50;
      color: white;
      border: none;
      padding: 0.75rem;
      border-radius: 4px;
      cursor: pointer;
      font-size: 1rem;
      transition: background 0.3s;
    }

    .add-button:hover {
      background: #45a049;
    }

    .task-list {
      display: flex;
      flex-direction: column;
      gap: 1rem;
    }

    .task-card {
      background: white;
      border-radius: 8px;
      padding: 1rem;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      border-left: 4px solid #ddd;
    }

    .priority-low { border-left-color: #4CAF50; }
    .priority-medium { border-left-color: #FFC107; }
    .priority-high { border-left-color: #f44336; }

    .task-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 0.5rem;
    }

    .task-header h3 {
      margin: 0;
      color: #333;
    }

    .done-button {
      background: #2196F3;
      color: white;
      border: none;
      padding: 0.5rem 1rem;
      border-radius: 4px;
      cursor: pointer;
      transition: background 0.3s;
    }

    .done-button:hover {
      background: #1976D2;
    }

    .task-description {
      color: #666;
      margin-bottom: 1rem;
    }

    .task-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 0.875rem;
    }

    .due-date {
      color: #666;
    }

    .priority-badge {
      padding: 0.25rem 0.5rem;
      border-radius: 4px;
      font-weight: 500;
      font-size: 0.75rem;
    }

    .priority-LOW .priority-badge { background: #E8F5E9; color: #4CAF50; }
    .priority-MEDIUM .priority-badge { background: #FFF8E1; color: #FFC107; }
    .priority-HIGH .priority-badge { background: #FFEBEE; color: #f44336; }
  `]
})
export class TaskListComponent implements OnInit {
  tasks: Task[] = [];

  constructor(private taskService: TaskService) {}

  async ngOnInit() {
    this.tasks = await this.taskService.getTasks();
  }

  async addTask(title: string, description: string, dueDate: string, priority: string) {
    if (!title || !description || !dueDate || !priority) return;

    const newTask: Task = {
      title,
      description,
      dueDate,
      priority: priority as 'LOW' | 'MEDIUM' | 'HIGH'
    };
    
    const addedTask = await this.taskService.addTask(newTask);
    this.tasks.unshift(addedTask);
  }

  async completeTask(id: number) {
    await this.taskService.completeTask(id);
    this.tasks = this.tasks.filter(task => task.id !== id);
  }
}
