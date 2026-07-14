<div align="center">

<img src="logo-unesp.png" alt="Logotipo da UNESP" width="170">

# BCC — UNESP Bauru

Exercícios, listas e projetos desenvolvidos durante a graduação em Ciência da Computação.

[![Java](https://img.shields.io/badge/Java-83%20arquivos-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](programacao-orientada-a-objetos)
[![C](https://img.shields.io/badge/C-12%20exerc%C3%ADcios-A8B9CC?style=for-the-badge&logo=c&logoColor=white)](estrutura-de-dados-1)
[![UNESP](https://img.shields.io/badge/UNESP-Bauru-008DD2?style=for-the-badge)](https://www.fc.unesp.br/)
[![Licença MIT](https://img.shields.io/badge/Licen%C3%A7a-MIT-2EA44F?style=for-the-badge)](LICENSE)

[Disciplinas](#disciplinas) · [Projetos em destaque](#projetos-em-destaque) · [Como executar](#como-executar) · [Organização](#organização)

</div>

## Sobre o repositório

Este repositório funciona como um arquivo acadêmico pessoal da graduação em **Ciência da Computação na UNESP, campus de Bauru**. O conteúdo está separado por disciplina e lista para facilitar a navegação, a revisão dos conceitos e o acompanhamento da evolução dos projetos.

## Disciplinas

| Disciplina | Linguagem | Conteúdo disponível |
| --- | --- | --- |
| [Estrutura de Dados I](estrutura-de-dados-1) | C | 2 listas, 12 exercícios |
| [Programação Orientada a Objetos](programacao-orientada-a-objetos) | Java | 8 listas, aplicações de terminal, Swing e persistência |

### Estrutura de Dados I

| Lista | Tópicos principais |
| --- | --- |
| [Lista 01](estrutura-de-dados-1/lista-01) | Inserção ordenada, listas encadeadas e lista duplamente encadeada |
| [Lista 02](estrutura-de-dados-1/lista-02) | Pilhas, filas, deques, escalonamento, expressões e simulações |

### Programação Orientada a Objetos

| Lista | Tópicos e projetos |
| --- | --- |
| [Lista 01](programacao-orientada-a-objetos/lista-01) | Fundamentos de Java, vetores, matrizes e `StudentsManager` |
| [Lista 02](programacao-orientada-a-objetos/lista-02) | Modelagem orientada a objetos e `DeliverySystem` no terminal |
| [Lista 03](programacao-orientada-a-objetos/lista-03) | Exceções, validação de entrada e encapsulamento de arrays |
| [Lista 04](programacao-orientada-a-objetos/lista-04) | Implementações de coleções e resolução de problemas |
| [Lista 05](programacao-orientada-a-objetos/lista-05) | Primeiras interfaces gráficas com Java Swing |
| [Lista 06](programacao-orientada-a-objetos/lista-06) | Eventos, calculadoras e editor de texto modular |
| [Lista 07](programacao-orientada-a-objetos/lista-07) | Evolução do `DeliverySystem` para uma interface Swing |
| [Lista 08](programacao-orientada-a-objetos/lista-08) | Persistência com HSQLDB, DAO e interface gráfica |

## Projetos em destaque

### Students Manager

Aplicação de terminal para cadastrar, buscar e ordenar alunos por nome, RA e coeficiente de rendimento. O projeto separa ações, modelos e validação de entrada.

[`programacao-orientada-a-objetos/lista-01/StudentsManager`](programacao-orientada-a-objetos/lista-01/StudentsManager)

### Delivery System

Projeto desenvolvido de forma incremental ao longo da disciplina:

1. [Versão de terminal](programacao-orientada-a-objetos/lista-02/DeliverySystem), com clientes, restaurantes, entregadores e pedidos.
2. [Versão com Java Swing](programacao-orientada-a-objetos/lista-07/DeliverySystemGui), com telas de cadastros e gerenciamento de pedidos.
3. [Versão com Swing e HSQLDB](programacao-orientada-a-objetos/lista-08/DeliverySystemGuiAndDB), com persistência local e camada DAO.

As instruções completas da versão com banco de dados estão no [README do projeto](programacao-orientada-a-objetos/lista-08/DeliverySystemGuiAndDB/README.md).

## Como executar

### Requisitos

- [JDK 11 ou superior](https://adoptium.net/temurin/releases/?version=11) para os projetos Java.
- GCC ou Clang para os exercícios em C.
- Git para clonar o repositório.

```bash
git clone https://github.com/matrodrigs/bcc-unesp.git
cd bcc-unesp
```

### Exemplo em Java

Cada lista é independente. Entre na pasta que contém a classe desejada antes de compilar:

```bash
cd programacao-orientada-a-objetos/lista-03
javac Solver.java
java Solver
```

Projetos com pacotes ou bibliotecas possuem comandos próprios. Consulte o README ou os arquivos da respectiva pasta antes da execução.

### Exemplo em C

```bash
cd estrutura-de-dados-1/lista-01
gcc Ex1.c -o ex1
./ex1
```

No Windows, execute o binário com `.\ex1.exe`. Alguns exercícios usam APIs específicas de Windows ou POSIX; nesses casos, compile no ambiente correspondente.

## Organização

```text
bcc-unesp/
├── estrutura-de-dados-1/
│   ├── lista-01/
│   └── lista-02/
├── programacao-orientada-a-objetos/
│   ├── lista-01/
│   ├── ...
│   └── lista-08/
├── logo-unesp.png
├── LICENSE
└── README.md
```

As pastas de disciplinas e listas usam nomes em `kebab-case`, sem espaços ou acentos. Os nomes das classes e dos projetos seguem as convenções das linguagens e foram preservados para não quebrar imports ou pontos de entrada.

## Tecnologias e conceitos

- **C:** ponteiros, alocação dinâmica, listas, pilhas, filas, deques e estruturas heterogêneas.
- **Java:** orientação a objetos, herança, polimorfismo, exceções, coleções e streams.
- **Desktop:** interfaces e eventos com Java Swing.
- **Persistência:** JDBC, HSQLDB e padrão DAO.
- **Ferramentas:** Git, linha de comando, Lombok, GCC e JDK.

## Licença

O conteúdo deste repositório está disponível sob a [Licença MIT](LICENSE).
