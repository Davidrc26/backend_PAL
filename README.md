ENDPOINTS PRUEBA Y ACTUALIZACION BASE DE DATOS

Crear en al base de datos el campo created_at para poner fecha de creacion de cursos automatica y poder aplicar filtros de busqueda de cursos
Los otros filtros de crean automaticamente por que tenemos spring.jpa.hibernate.ddl-auto=update
ALTER TABLE courses ADD COLUMN created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;


Buscar cursos por palabra clave, por nombre, descripcion, varias palabras de la descripcion o del nombre, o por categoria
http://localhost:8081/api/courses/search?q=java

Filtrar por cursos gratuitos y que tengan coincidencia java
http://localhost:8080/api/courses/search?q=java&free=true

Filtrar por cursos pagos
http://localhost:8080/api/courses/search?q=java&free=false

Filtrar por nivel de dificultad
http://localhost:8080/api/courses/search?q=java&difficulty=avanzado
http://localhost:8080/api/courses/search?q=java&difficulty=intermedio
http://localhost:8080/api/courses/search?q=java&difficulty=básico

Filtrar por calificación promedio mínima
http://localhost:8080/api/courses/search?q=java&minRating=4.5

Ordenar resultados por fecha de publicación (más recientes primero)
http://localhost:8080/api/courses/search?q=java&orderBy=date

Combinando varios filtros (SOLO ES OBLIGATORIO EL FILTRO = q)
http://localhost:8080/api/courses/search?q=java&free=true&difficulty=avanzado&minRating=4.5&orderBy=date
