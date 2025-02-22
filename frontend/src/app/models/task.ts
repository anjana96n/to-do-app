export interface Task {
    id?: number;
    title: string;
    description: string;
    dueDate: string;
    priority: 'LOW' | 'MEDIUM' | 'HIGH';
    completed?: boolean;
  }
  