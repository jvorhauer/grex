package grex.orm;

public record Column(String name, String dbType, int length, String javaType) {
}
