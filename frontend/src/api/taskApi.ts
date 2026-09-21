import type { CreateTaskRequest, Task } from "@/types/task";

async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const body = await response.json().catch(() => null);
    throw new Error(body?.message ?? `Request failed with status ${response.status}`);
  }
  return response.json() as Promise<T>;
}

export const taskApi = {
  async list(): Promise<Task[]> {
    const response = await fetch("/api/tasks");
    return handleResponse<Task[]>(response);
  },

  async get(id: string): Promise<Task> {
    const response = await fetch(`/api/tasks/${id}`);
    return handleResponse<Task>(response);
  },

  async create(request: CreateTaskRequest): Promise<Task> {
    const response = await fetch("/api/tasks", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(request),
    });
    return handleResponse<Task>(response);
  },
};
