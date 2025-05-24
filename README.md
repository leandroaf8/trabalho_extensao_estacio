# trabalho_extensao
Trabalho de extensão Estácio
# App Lembrete de Validade de Produtos

Este é um aplicativo Android simples desenvolvido em Java que ajuda você a controlar a validade dos seus produtos. Com ele, você pode registrar os produtos informando o nome e a data de validade, e acompanhar facilmente o tempo restante para o vencimento de cada item.

## Principais Funcionalidades

- **Cadastro de Produtos:** Registre produtos pelo nome e data de validade (formato brasileiro dd/MM/yyyy).
- **Lista de Produtos:** Visualize todos os produtos cadastrados em uma lista, com contadores regressivos que exibem quantos dias faltam para o vencimento.
- **Editar e Excluir Produtos:** Acesse opções para editar ou excluir um produto ao pressionar e segurar o item por meio segundo.
- **Temas Claro e Escuro:** Altere o tema do aplicativo por meio de um menu, alternando entre os modos claro e escuro para melhor visualização.
- **Persistência de Dados:** Os dados dos produtos são armazenados localmente usando banco de dados SQLite, garantindo que as informações persistam entre sessões.

## Tecnologias Utilizadas

- **Linguagem:** Java
- **Plataforma:** Android
- **Armazenamento:** SQLite (banco de dados local)
- **Interface:** RecyclerView para exibição da lista, AlertDialogs para seleção do tema e edição de produtos.
- **Arquitetura:** Padrão MVC aproximado com separação entre modelo (dados), visão (interface) e controlador (lógica).

## Como Usar

1. Abra o app e cadastre um produto informando o nome e a data de validade.
2. A lista mostrará o nome do produto e o tempo restante para o vencimento.
3. Para editar ou excluir um produto, toque e segure o item por meio segundo e escolha a ação desejada.
4. Altere o tema do app clicando no ícone de menu no canto superior direito e selecionando o tema claro ou escuro.

## Requisitos

- Android Studio
- Dispositivo ou emulador Android com versão compatível

## Como Executar

1. Clone este repositório.
2. Abra o projeto no Android Studio.
3. Compile e execute o app em um emulador ou dispositivo.

## Contato

Se tiver dúvidas ou quiser contribuir, fique à vontade para abrir uma issue ou enviar um pull request.

---

Este projeto é uma boa base para quem deseja aprender a manipular banco de dados SQLite em Android e implementar temas dinâmicos e interfaces intuitivas.

