import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { TaskListComponent } from './task-list.component';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task';

describe('TaskListComponent', () => {
  let component: TaskListComponent;
  let fixture: ComponentFixture<TaskListComponent>;
  let taskService: jasmine.SpyObj<TaskService>;

  const mockTasks: Task[] = [
    {
      id: 1,
      title: 'Test Task',
      description: 'Test Description',
      dueDate: '2024-03-20',
      priority: 'MEDIUM',
      completed: false
    }
  ];

  beforeEach(async () => {
    // Create spy object for TaskService
    taskService = jasmine.createSpyObj('TaskService', ['getTasks', 'addTask', 'completeTask']);
    
    // Configure default spy behavior
    taskService.getTasks.and.returnValue(Promise.resolve(mockTasks));
    taskService.addTask.and.returnValue(Promise.resolve(mockTasks[0]));
    taskService.completeTask.and.returnValue(Promise.resolve());

    await TestBed.configureTestingModule({
      imports: [TaskListComponent, FormsModule],
      providers: [
        { provide: TaskService, useValue: taskService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TaskListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load tasks on init', async () => {
    await component.ngOnInit();
    expect(taskService.getTasks).toHaveBeenCalled();
    expect(component.tasks).toEqual(mockTasks);
  });

  it('should not add task when title is empty', async () => {
    const taskForm = {
      value: {
        title: '',
        description: 'Test Description',
        dueDate: '2024-03-20',
        priority: 'MEDIUM'
      },
      reset: jasmine.createSpy('reset'),
      form: {
        controls: {
          priority: {
            setValue: jasmine.createSpy('setValue')
          }
        }
      }
    };

    await component.addTask(taskForm);
    expect(component.titleError).toBe('Title should not be empty');
    expect(taskService.addTask).not.toHaveBeenCalled();
  });

  it('should add task successfully', async () => {
    const newTask: Task = {
      title: 'New Task',
      description: 'New Description',
      dueDate: '2024-03-20',
      priority: 'HIGH'
    };

    const taskForm = {
      value: newTask,
      reset: jasmine.createSpy('reset'),
      form: {
        controls: {
          priority: {
            setValue: jasmine.createSpy('setValue')
          }
        }
      }
    };

    taskService.addTask.and.returnValue(Promise.resolve({ ...newTask, id: 2 }));

    await component.addTask(taskForm);
    
    expect(taskService.addTask).toHaveBeenCalledWith(newTask);
    expect(taskForm.reset).toHaveBeenCalled();
    expect(taskForm.form.controls.priority.setValue).toHaveBeenCalledWith('MEDIUM');
    expect(component.titleError).toBe('');
  });

  it('should complete task successfully', async () => {
    const taskId = 1;
    await component.completeTask(taskId);
    
    expect(taskService.completeTask).toHaveBeenCalledWith(taskId);
    expect(component.tasks.find(t => t.id === taskId)).toBeUndefined();
  });

  it('should handle error when adding task', async () => {
    const taskForm = {
      value: {
        title: 'Test Task',
        description: 'Test Description',
        dueDate: '2024-03-20',
        priority: 'MEDIUM'
      },
      reset: jasmine.createSpy('reset'),
      form: {
        controls: {
          priority: {
            setValue: jasmine.createSpy('setValue')
          }
        }
      }
    };

    taskService.addTask.and.rejectWith(new Error('Network error'));
    spyOn(console, 'error');

    await component.addTask(taskForm);
    expect(console.error).toHaveBeenCalledWith('Error adding task:', jasmine.any(Error));
  });

  it('should track tasks by id', () => {
    const task = mockTasks[0];
    expect(component.trackTask(0, task)).toBe(task.id || 0);
  });
});
