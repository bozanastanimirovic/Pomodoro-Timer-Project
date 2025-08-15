import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TASK_URL } from '../constants'; 
import { Task } from '../models/task';  
import { TaskDTO } from '../models/DTO/task-dto'; 

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(private httpClient: HttpClient) { }

  getAllTasks(): Observable<any> {
    return this.httpClient.get(`${TASK_URL}/`);
  }

  createTask(taskDTO: TaskDTO): Observable<any> {
    return this.httpClient.post(`${TASK_URL}/`, taskDTO);
  }

  updateTaskName(id: number, taskName: string): Observable<Task> {
    return this.httpClient.put<Task>(`${TASK_URL}/name/${id}`, taskName);
  }

  toggleFinishedTask(id: number): Observable<Task> {
    return this.httpClient.put<Task>(`${TASK_URL}/toggle-finished/${id}`, {});
  }

  deleteTask(id: number): Observable<any> {
    return this.httpClient.delete(`${TASK_URL}/${id}`, { responseType: 'text' });
  }

  getTasksByUserId(userId: number): Observable<any> {
    return this.httpClient.get(`${TASK_URL}/user/${userId}`);
  }
}
