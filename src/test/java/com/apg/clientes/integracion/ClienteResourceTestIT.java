package com.apg.clientes.integracion;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
public class ClienteResourceTestIT {

    // Crea cliente y retorna Id, Se estaba repitiendo mucho en los test
    private Long crearClienteRetornaId(String jsonBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/clientes")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");
    }

    @Test
    void crearClienteDeberiaRetornar201() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "primerNombre": "Juan",
                    "segundoNombre": "Luis",
                    "primerApellido": "Perez",
                    "segundoApellido": "Gomez",
                    "correo": "juan.perez@test.com",
                    "direccion": "Calle 1",
                    "telefono": "8091234567",
                    "pais": "DOM"
                }
            """)
                .when()
                .post("/clientes")
                .then()
                .statusCode(201)
                .body("primerNombre", equalTo("Juan"))
                .body("segundoNombre", equalTo("Luis"))
                .body("primerApellido", equalTo("Perez"))
                .body("segundoApellido", equalTo("Gomez"))
                .body("correo", equalTo("juan.perez@test.com"))
                .body("direccion", equalTo("Calle 1"))
                .body("telefono", equalTo("8091234567"))
                .body("pais", equalTo("DOM"))
                .body("gentilicio", containsString("Masculino: Dominican"))
                .body("gentilicio", containsString("Femenino: Dominican"));
    }

    @Test
    void crearClienteErrorValidacionDeberiaRetornar400() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "primerNombre": "",
                    "correo": "estonoesuncorreo",
                    "pais": "D",
                    "telefono": "12"
                }
            """)
                .when()
                .post("/clientes")
                .then()
                .statusCode(400)
                .body("mensaje", containsString("Errores de validacion"))
                .body("detalles", hasItem(containsString("El primer nombre es obligatorio")))
                .body("detalles", hasItem(containsString("El correo no es valido")))
                .body("detalles", hasItem(containsString("La direccion es obligatoria")))
                .body("detalles", hasItem(containsString("El telefono tiene un formato incorrecto")))
                .body("detalles", hasItem(containsString("formato ISO 3166")));
    }

    @Test
    void crearClienteCorreoDuplicadoDeberiaRetornar400() {
        crearClienteRetornaId("""
            {
                "primerNombre": "Juan",
                "primerApellido": "Perez",
                "correo": "test@duplicado.com",
                "direccion": "Calle 1",
                "telefono": "8091111112",
                "pais": "DOM"
            }
        """);

        given()
                .contentType(ContentType.JSON)
                .body("""
            {
                "primerNombre": "Raul",
                "primerApellido": "Perez",
                "correo": "test@duplicado.com",
                "direccion": "Calle 2",
                "telefono": "8092222222",
                "pais": "DOM"
            }
        """)
                .when()
                .post("/clientes")
                .then()
                .statusCode(400)
                .body("mensaje", containsString("Este correo ya ha sido registrado"));
    }

    @Test
    void ObtenerTodosDeberiaRetornar200() {

        given()
                .when()
                .get("/clientes")
                .then()
                .statusCode(200)
                .body("$", notNullValue());
    }

    @Test
    void ObtenerPorIdDeberiaRetornar200() {
        Long id = crearClienteRetornaId("""
                {
                    "primerNombre": "Carlos",
                    "primerApellido": "Peña",
                    "correo": "carlos.peña@test.com",
                    "direccion": "Calle 2",
                    "telefono": "+1 8092222222",
                    "pais": "DOM"
                }
            """);

        given()
                .when()
                .get("/clientes/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id.intValue()))
                .body("primerNombre", equalTo("Carlos"))
                .body("primerApellido", equalTo("Peña"))
                .body("correo", equalTo("carlos.peña@test.com"))
                .body("direccion", equalTo("Calle 2"))
                .body("telefono", equalTo("+1 8092222222"))
                .body("pais", equalTo("DOM"))
                .body("gentilicio", containsString("Masculino: Dominican"))
                .body("gentilicio", containsString("Femenino: Dominican"));
    }

    @Test
    void ObtenerPorIdNoEncontradoDeberiaRetornar404() {
        given()
                .when()
                .get("/clientes/999999")
                .then()
                .statusCode(404)
                .body("mensaje", containsString("no fue encontrado"));
    }

    @Test
    void ObtenerPorPaisDeberiaRetornar200() {
        Long id = crearClienteRetornaId("""
                {
                    "primerNombre": "´Gabriel",
                    "primerApellido": "Vasquez",
                    "correo": "gabriel.vasquez@test.com",
                    "direccion": "Calle 7",
                    "telefono": "8097777777",
                    "pais": "DOM"
                }
            """);

        given()
                .when()
                .get("/clientes/pais/DOM")
                .then()
                .statusCode(200)
                .body("$", anyOf(notNullValue(), hasSize(0)));
    }

    @Test
    void actualizarClienteDeberiaRetornar200() {
        Long id = crearClienteRetornaId("""
                {
                    "primerNombre": "Pedro",
                    "primerApellido": "Mella",
                    "correo": "pedro.mella@test.com",
                    "direccion": "Calle 3",
                    "telefono": "8093333333",
                    "pais": "DOM"
                }
            """);

        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "correo": "pedro.mella@ejemplo.com",
                    "direccion": "Nueva calle",
                    "telefono": "8090000000",
                    "pais": "MEX"
                }
            """)
                .when()
                .put("/clientes/" + id)
                .then()
                .statusCode(200)
                .body("correo", equalTo("pedro.mella@ejemplo.com"))
                .body("direccion", equalTo("Nueva calle"))
                .body("telefono", equalTo("8090000000"))
                .body("pais", equalTo("MEX"))
                .body("gentilicio", containsString("Masculino: Mexican"))
                .body("gentilicio", containsString("Femenino: Mexican"));
    }

    @Test
    void actualizarClienteNoEncontradoDeberiaRetornar404() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "correo": "noexiste@test.com",
                    "direccion": "calle nadie sabe",
                    "telefono": "8091111111",
                    "pais": "DOM"
                }
            """)
                .when()
                .put("/clientes/999999")
                .then()
                .statusCode(404)
                .body("mensaje", containsString("no fue encontrado"));
    }

    @Test
    void parchearClienteDeberiaRetornar200() {
        Long id = crearClienteRetornaId("""
                {
                    "primerNombre": "Tony",
                    "primerApellido": "Rosario",
                    "correo": "tony.rosario@test.com",
                    "direccion": "Calle 4",
                    "telefono": "8094444444",
                    "pais": "DOM"
                }
            """);

        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "correo": "tony.rosario@prueba.com",
                    "direccion": "Nueva calle 2"
                }
            """)
                .when()
                .patch("/clientes/" + id)
                .then()
                .statusCode(200)
                .body("correo", equalTo("tony.rosario@prueba.com"))
                .body("direccion", equalTo("Nueva calle 2"))
                .body("telefono", equalTo("8094444444"))
                .body("pais", equalTo("DOM"))
                .body("gentilicio", containsString("Masculino: Dominican"))
                .body("gentilicio", containsString("Femenino: Dominican"));
    }

    @Test
    void parchearClienteNoEncontradoDeberiaRetornar404() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "correo": "noexiste@prueba.com",
                    "direccion": "calle nadie sabe",
                    "telefono": "8091111111",
                    "pais": "DOM"
                }
            """)
                .when()
                .put("/clientes/999999")
                .then()
                .statusCode(404)
                .body("mensaje", containsString("no fue encontrado"));
    }

    @Test
    void eliminarClienteDeberiaRetornar204() {
        Long id = crearClienteRetornaId("""
                {
                    "primerNombre": "Ana",
                    "primerApellido": "Mercedes",
                    "correo": "ana.mercedes@test.com",
                    "direccion": "Calle 5",
                    "telefono": "8095555555",
                    "pais": "DOM"
                }
            """);

        given()
                .when()
                .delete("/clientes/" + id)
                .then()
                .statusCode(204);
    }

    @Test
    void eliminarClienteNoEncontradoDeberiaRetornar404() {
        given()
                .when()
                .delete("/clientes/999999")
                .then()
                .statusCode(404)
                .body("mensaje", containsString("no fue encontrado"));
    }

}
