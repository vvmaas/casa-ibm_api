# casa-ibm_api

Essa aplicação Spring Boot foi desenvolvida para viabilizar a reserva de hospedagens em uma casa de temporada. Conta com recursos que facilitam a manipulação e listagem das informações referentes às reservas.

### Link deploy: https://casa-ibm-web.vercel.app

Requisições nessa aplicação podem demorar até 2 minutos para responder devido ao deploy feito em modalidade gratuita do Render.

### Link repositório front-end: https://github.com/vvmaas/casa-ibm_web


## Configuração e instalação

Siga estas etapas para configurar e executar o back-end:

1. Certifique-se de ter o Java Development Kit (JDK) instalado em seu computador. Baixe-o em https://www.oracle.com/java/technologies/javase-jdk11-downloads.html.

2. Clone este repositório para seu ambiente local.

3. Importe o projeto como um projeto Maven existente em sua IDE de desenvolvimento.

4. Inicie o aplicativo em sua IDE de desenvolvimento através do arquivo `CasaIbmApplication.java`.

5. A aplicação será executada na porta 8080.


## Utilização

### POST /reservas - Criar reserva

Para criar uma reserva, a requisição POST deve ser enviada para:

```url
http://localhost:8080/reservas
```
Com o body da requisição no formato: 

```json
{
    "nomeHospede": "Gabriel Toledo",  //minímo de 3 dígitos 
    "dataInicio": "2023-08-10",
    "dataFim": "2023-08-15",
    "quantidadePessoas": 2    //minímo 1 
}
```
A rota retorna então:

```json
{
    "id": 1,
    "nomeHospede": "Gabriel Toledo",
    "dataInicio": "2023-08-10",
    "dataFim": "2023-08-15",
    "quantidadePessoas": 2,
    "status": "CONFIRMADA"
}
```

### GET /reservas - Buscar todas as reservas

Para buscar todas as reservas, a requisição GET deve ser enviada para:

```url
http://localhost:8080/reservas
```
A rota irá retornar:

```json
[
{
    "id": 1,
    "nomeHospede": "Gabriel Toledo",
    "dataInicio": "2023-08-10",
    "dataFim": "2023-08-15",
    "quantidadePessoas": 2,
    "status": "CONFIRMADA"
},
{
    "id": 2,
    "nomeHospede": "Fernando Alvarenga",
    "dataInicio": "2023-08-16",
    "dataFim": "2023-08-21",
    "quantidadePessoas": 3,
    "status": "PENDENTE"
},
...
]

```

### GET /reservas/{id} - Buscar reserva por id

Para buscar uma única reserva utilizando seu id, envie requisição GET para:

```url
http://localhost:8080/reservas/{id}
```
Retorno:

```json
{
    "id": 1,
    "nomeHospede": "Gabriel Toledo",
    "dataInicio": "2023-08-10",
    "dataFim": "2023-08-15",
    "quantidadePessoas": 2,
    "status": "CONFIRMADA"
}
```

### PUT /reservas/{id} - Alterar dados de uma reserva

Para modificar os dados de uma reserva existente, envie requisição PUT para:

```url
http://localhost:8080/reservas/{id}
```
Com o body:

```json
{
    "nomeHospede": "Gabriel Alcântara",
    "dataInicio": "2023-08-11",
    "dataFim": "2023-08-16",
    "quantidadePessoas": 1,
    "status": "PENDENTE"
}
```

Retorno:

```json
{
    "id": 1,
    "nomeHospede": "Gabriel Alcântara",
    "dataInicio": "2023-08-11",
    "dataFim": "2023-08-16",
    "quantidadePessoas": 1,
    "status": "PENDENTE"
}
```

### DELETE /reservas/{id}/cancelar - Cancelar uma reserva

Para realizar o cancelamento de uma reserva, envie requisição DELETE para:

```url
http://localhost:8080/reservas/{id}/cancelar
```
Retorno:

```json
{
    "id": 1,
    "nomeHospede": "Gabriel Toledo",
    "dataInicio": "2023-08-10",
    "dataFim": "2023-08-15",
    "quantidadePessoas": 2,
    "status": "CANCELADA"
}
```
```Essa ação não é reversível.```

### GET /dias-reservados - Buscar por dias ocupados

Para consultar dias que já possuem reservas, envie requisição GET para: 

```url
http://localhost:8080/dias-reservados
```
Retorno:

```json
[
  {
    "id": 1,
    "dia": "2023-08-07"
  },
  {
    "id": 2,
    "dia": "2023-08-08"
  },
  {
    "id": 3,
    "dia": "2023-08-09"
  },
  {
    "id": 4,
    "dia": "2023-08-12"
  },
  {
    "id": 5,
    "dia": "2023-08-13"
  },
  ...
]
```

