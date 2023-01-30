package umm3601.todo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TodoDatabase {

  private Todo[] allTodos;

  public TodoDatabase(String todoDataFile) throws IOException {
    InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(todoDataFile));
    ObjectMapper objectMapper = new ObjectMapper();
    allTodos = objectMapper.readValue(reader, Todo[].class);
  }

  public int size() {
    return allTodos.length;
  }

  public Todo getTodo(String id) {
    return Arrays.stream(allTodos).filter(x -> x._id.equals(id)).findFirst().orElse(null);
  }

  public Todo[] listTodos(Map<String, List<String>> queryParams) {
    Todo[] filteredTodos = allTodos;

    if (queryParams.containsKey("owner")) {
      String targetOwner = queryParams.get("owner").get(0);
      filteredTodos = filterTodosByOwner(filteredTodos, targetOwner);
    }
    if (queryParams.containsKey("category")) {
      String targetCategory = queryParams.get("category").get(0);
      filteredTodos = filterTodosByCategory(filteredTodos, targetCategory);
    }
    if (queryParams.containsKey("contains")) {
      String targetContains = queryParams.get("contains").get(0);
      filteredTodos = filterTodosByContains(filteredTodos, targetContains);
    }
    if (queryParams.containsKey("status")) {
      boolean targetBoolean = false;
      String targetBody = queryParams.get("status").get(0);
      if (targetBody.equals("complete")) {
        targetBoolean = true;
      }
      filteredTodos = filterTodosByStatus(filteredTodos, targetBoolean);
    }
    if (queryParams.containsKey("orderBy")) {
      String targetOrder = queryParams.get("orderBy").get(0);
      filteredTodos = orderBy(filteredTodos, targetOrder);
    }

    if (queryParams.containsKey("limit")) {
      String targetLimit = queryParams.get("limit").get(0);
      filteredTodos = orderBy(filteredTodos, targetLimit);
    }
      return filteredTodos;
    }

  public Todo getTodos(String id) {
    return Arrays.stream(allTodos).filter(x -> x._id.equals(id)).findFirst().orElse(null);
  }

  public Todo[] filterTodosByCategory(Todo[] todos, String targetCategory) {
    return Arrays.stream(todos).filter(x -> x.category.equals(targetCategory)).toArray(Todo[]::new);
  }

  public Todo[] filterTodosBy_id(Todo[] todos, String target_id) {
    return Arrays.stream(todos).filter(x -> x._id.equals(target_id)).toArray(Todo[]::new);
  }

  public Todo[] filterTodosByOwner(Todo[] todos, String targetOwner) {
    return Arrays.stream(todos).filter(x -> x.owner.equals(targetOwner)).toArray(Todo[]::new);
  }

  public Todo[] filterTodosByStatus(Todo[] todos, boolean targetStatus) {
    return Arrays.stream(todos).filter(x -> x.status.equals(targetStatus)).toArray(Todo[]::new);
  }

  public Todo[] filterTodosByContains(Todo[] todos, String targetContains) {
    return Arrays.stream(todos).filter(x -> x.body.contains(targetContains)).toArray(Todo[]::new);
  }

  public Todo[] filterTodosByLimit(Todo[] todos, String targetLimit) {
    return Arrays.stream(todos).limit(Integer.valueOf(targetLimit)).toArray(Todo[]::new);
  }


  public Todo[] orderBy(Todo[] todos, String targetOrder) {
    if (targetOrder.equals("owner")) {
      return Arrays.stream(todos).sorted(Comparator.comparing((Todo t) -> t.owner)).toArray(Todo[]::new);
    } else if (targetOrder.equals("body")) {
      return Arrays.stream(todos).sorted(Comparator.comparing((Todo t) -> t.body)).toArray(Todo[]::new);
    } else if (targetOrder.equals("status")) {
      return Arrays.stream(todos).sorted(Comparator.comparing((Todo t) -> t.status)).toArray(Todo[]::new);
    } else {
      return Arrays.stream(todos).sorted(Comparator.comparing((Todo t) -> t.category)).toArray(Todo[]::new);
    }
  }


}
