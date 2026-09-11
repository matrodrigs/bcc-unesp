## Como executar

Abra um terminal nesta pasta e compile os arquivos:

```powershell
javac *.java
```

Inicie o servidor:

```powershell
java Server
```

Depois, abra outros dois terminais e execute em cada um:

```powershell
java Client
```

Por padrão, a aplicação usa `127.0.0.1` e a porta `5000`. Para usar outro computador ou outra porta:

```powershell
java Server 6000
java Client 192.168.0.10 6000
```