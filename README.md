# Tokio Transfer API

Serviço de agendamento de transferências com cálculo de taxa por faixa de dias **D** (diferença entre **hoje** e a `transferDate`, fuso `America/Sao_Paulo`).

## Tecnologias
- Java 11 (Eclipse Adoptium)
- Spring Boot (Web, Validation, Data JPA)
- JPA/Hibernate

> **Observação:** a data de agendamento (`scheduledDate`) é definida pelo domínio como **hoje** (fuso SP) na criação e não é informada pelo cliente.

## Regras de negócio (resumo)
- `D = daysBetween(hojeBR, transferDate)`
- Se `transferDate < hojeBR` → **422** “A data da transferência deve ser hoje ou futura.”
- Se `D < 0` ou `D > 50` → **422** “Sem taxa aplicável para a data informada.”
- Arredondamento de taxa: **2 casas**, `HALF_UP`.

### Tabela de taxa
- `D = 0` → **R$ 3,00 + 2,50%**
- `1 ≤ D ≤ 10` → **R$ 12,00**
- `11 ≤ D ≤ 20` → **8,2%**
- `21 ≤ D ≤ 30` → **6,9%**
- `31 ≤ D ≤ 40` → **4,7%**
- `41 ≤ D ≤ 50` → **1,7%**

## Como acessar o console (H2)

### Suba a aplicação:

```
mvn spring-boot:run
```

### Abra no navegador:

http://localhost:8080/h2-console


### Preencha os campos:

**JDBC URL**: ```jdbc:jdbc:h2:mem:tokio```

***User Name***: ```tokio```

***Password***: ```(vazio)```

### Consulta rápida para verificar dados:

SELECT * FROM transfers;

## Como usar o Swagger

### Abra no navegador
http://localhost:8080/swagger-ui/index.html

## Endpoints

### POST `/transfers`
Cria uma transferência.

**Request body**
```json
{
  "sourceAccount": "1234567890",
  "destinationAccount": "0987654321",
  "amount": 1200.50,
  "transferDate": "2025-09-10"
}
```

### GET `/transfers`
Veja todas as transferências adicionadas.
