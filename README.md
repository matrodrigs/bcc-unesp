# BCC · UNESP Bauru

<p><samp>Ciência da Computação · Arquivo acadêmico</samp></p>

Exercícios, listas e projetos desenvolvidos durante a graduação na **UNESP, campus de Bauru**.
O conteúdo acompanha o estudo de hardware digital, estruturas de dados em C e orientação a
objetos em Java, incluindo interfaces Swing e persistência com HSQLDB.

[Disciplinas](#disciplinas) · [Projetos em destaque](#projetos-em-destaque) · [Como executar](#como-executar)

## Disciplinas

| Disciplina | Linguagem | Conteúdo disponível |
| --- | --- | --- |
| [Arquitetura de Computadores](arquitetura-de-computadores) | Hardware digital | [Diagrama de computador com 8031](arquitetura-de-computadores/projeto-computador-8031.pdf) |
| [Estrutura de Dados I](estrutura-de-dados-1) | C | 2 listas, 12 exercícios |
| [Programação Orientada a Objetos](programacao-orientada-a-objetos) | Java | 8 listas, aplicações de terminal, Swing e persistência |

### Estrutura de Dados I

| Lista | Tópicos principais |
| --- | --- |
| [Lista 01](estrutura-de-dados-1/lista-01) | Inserção ordenada, listas encadeadas e lista duplamente encadeada |
| [Lista 02](estrutura-de-dados-1/lista-02) | Pilhas, filas, deques, escalonamento, expressões e simulações |

### Programação Orientada a Objetos

Material de apoio: [apostila de POO em Java](programacao-orientada-a-objetos/apostila-poo-java.pdf).

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

Cada lista é independente. A partir da raiz do repositório, entre na pasta da classe desejada:

```bash
cd programacao-orientada-a-objetos/lista-03
javac Solver.java
java Solver
```

Projetos com pacotes ou bibliotecas possuem comandos próprios. Consulte o README ou os arquivos da respectiva pasta antes da execução.

### Exemplo em C

A partir da raiz do repositório:

```bash
cd estrutura-de-dados-1/lista-01
gcc Ex1.c -o ex1
./ex1
```

No Windows, execute o binário com `.\ex1.exe`. Alguns exercícios usam APIs específicas de Windows ou POSIX; nesses casos, compile no ambiente correspondente.

## Organização

Use as tabelas de disciplinas para chegar à lista ou ao projeto desejado. As pastas seguem
`disciplina/lista/projeto`, quando há um projeto completo; exercícios avulsos ficam diretamente
na lista. Cada programa é compilado separadamente.

Os diretórios de disciplinas e listas usam `kebab-case`, sem espaços ou acentos. Classes e
projetos preservam os nomes das linguagens. Instruções específicas ficam junto ao respectivo
projeto, como no [Delivery System com HSQLDB](programacao-orientada-a-objetos/lista-08/DeliverySystemGuiAndDB/README.md).

## Licença

O conteúdo deste repositório está disponível sob a [Licença MIT](LICENSE).

<sub>Organizado por <a href="https://github.com/matrodrigs">Mateus Rodrigues</a>.</sub>
