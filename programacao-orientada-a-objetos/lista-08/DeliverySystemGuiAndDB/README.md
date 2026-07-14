# Delivery System — Swing e HSQLDB

Aplicação desktop para gerenciar clientes, restaurantes, entregadores e pedidos. Esta versão combina uma interface gráfica em Java Swing com persistência local em HSQLDB.

## Funcionalidades

- Cadastro e atualização de clientes, restaurantes e entregadores.
- Cadastro de pratos no cardápio dos restaurantes.
- Criação de pedidos com cálculo do valor total.
- Atribuição de entregadores disponíveis.
- Atualização do status dos pedidos.
- Persistência e carregamento automático dos dados.

## Requisitos

- JDK 11 ou superior.
- Terminal aberto na raiz desta pasta.

As dependências necessárias já estão incluídas:

- `lib/lombok.jar`
- `db/hsqldb.jar`

## Compilar e executar

### Windows — PowerShell ou Prompt de Comando

```powershell
javac -cp ".;lib/lombok.jar;db/hsqldb.jar" Main.java core\*.java db\*.java db\dao\*.java gui\*.java gui\tabs\*.java models\*.java
java -cp ".;lib/lombok.jar;db/hsqldb.jar" Main
```

### Linux ou macOS

```bash
javac -cp ".:lib/lombok.jar:db/hsqldb.jar" Main.java core/*.java db/*.java db/dao/*.java gui/*.java gui/tabs/*.java models/*.java
java -cp ".:lib/lombok.jar:db/hsqldb.jar" Main
```

O caminho do banco é relativo à pasta do projeto. Execute os comandos acima sem mudar o diretório de trabalho para que `db/dados/bancodados` seja localizado corretamente.

## Gerenciador visual do banco

No Windows, o HSQLDB Database Manager pode ser aberto pelo script incluído:

```powershell
cd db
.\iniciarManagerSwing.bat
```

## Estrutura

```text
DeliverySystemGuiAndDB/
├── core/        # Regras do sistema e coordenação dos casos de uso
├── db/          # Conexão, DAOs, arquivos do banco e HSQLDB
├── gui/         # Janela principal e abas Swing
├── lib/         # Lombok
├── models/      # Entidades do domínio
└── Main.java    # Ponto de entrada
```

Os arquivos persistidos pelo HSQLDB ficam em `db/dados/`. Arquivos temporários gerados durante a execução são ignorados pelo Git.
