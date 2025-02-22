import { Injectable } from '@angular/core';
import axios from 'axios';
import { Task } from '../models/task';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private apiUrl = 'http://localhost:8080/api/tasks';

  async getTasks(): Promise<Task[]> {
    const response = await axios.get<Task[]>(this.apiUrl);
    return response.data;
  }

  async addTask(task: Task): Promise<Task> {
    const response = await axios.post<Task>(this.apiUrl, task);
    return response.data;
  }

  async completeTask(id: number): Promise<void> {
    await axios.put(`${this.apiUrl}/${id}/done`);
  }
}
