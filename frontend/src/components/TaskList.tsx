import { useEffect, useState } from "react";
import { taskApi } from "@/api/taskApi";
import type { Task } from "@/types/task";
import "./TaskList.css";

export function TaskList() {
  const [tasks, setTasks] = useState<Task[] | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;

    taskApi
      .list()
      .then((result) => {
        if (!cancelled) setTasks(result);
      })
      .catch((err: unknown) => {
        if (!cancelled) setError(err instanceof Error ? err.message : "Failed to load tasks");
      });

    return () => {
      cancelled = true;
    };
  }, []);

  if (error) {
    return (
      <p role="alert" className="task-list__error">
        {error}
      </p>
    );
  }

  if (tasks === null) {
    return (
      <p aria-live="polite" className="task-list__loading">
        Loading tasks…
      </p>
    );
  }

  if (tasks.length === 0) {
    return <p className="task-list__empty">No tasks yet. Create one to get started.</p>;
  }

  return (
    <ul className="task-list" aria-label="Tasks">
      {tasks.map((task) => (
        <li key={task.id} className="task-list__item">
          <span className="task-list__title">{task.title}</span>
          <span className="task-list__status" aria-label={`Status: ${task.status}`}>
            {task.status}
          </span>
        </li>
      ))}
    </ul>
  );
}
