import { render, screen } from "@testing-library/react";
import { afterEach, describe, expect, it, vi } from "vitest";
import { taskApi } from "@/api/taskApi";
import { TaskList } from "./TaskList";

vi.mock("@/api/taskApi");

describe("TaskList", () => {
  afterEach(() => {
    vi.resetAllMocks();
  });

  it("shows a loading state before tasks arrive", () => {
    vi.mocked(taskApi.list).mockReturnValue(new Promise(() => {}));

    render(<TaskList />);

    expect(screen.getByText(/loading tasks/i)).toBeInTheDocument();
  });

  it("renders tasks once loaded", async () => {
    vi.mocked(taskApi.list).mockResolvedValue([
      { id: "1", title: "Write docs", description: null, status: "OPEN", createdAt: "", updatedAt: null },
    ]);

    render(<TaskList />);

    expect(await screen.findByText("Write docs")).toBeInTheDocument();
  });

  it("shows an empty state when there are no tasks", async () => {
    vi.mocked(taskApi.list).mockResolvedValue([]);

    render(<TaskList />);

    expect(await screen.findByText(/no tasks yet/i)).toBeInTheDocument();
  });

  it("shows an error message when the request fails", async () => {
    vi.mocked(taskApi.list).mockRejectedValue(new Error("Network error"));

    render(<TaskList />);

    expect(await screen.findByRole("alert")).toHaveTextContent("Network error");
  });
});
