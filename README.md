# Desafio na Cozinha 

**Algoritmos e Estruturas de Dados II**

Repositório: [🔗](https://github.com/FelipeBellomo/Desafio-Cozinha-aed2/tree/dev)

## Integrantes
Carlos Felipe Bellomo \
Paulo Henrique Anesi Giacomelli

## Instruções de Execução (Qualquer SO)
O projeto utiliza o [**Maven**](https://maven.apache.org/install.html) para gerenciamento de dependências e build, o que garante que ele possa ser executado facilmente em qualquer sistema operacional (Windows, Linux, macOS).

### Pré-requisitos
* **Java Development Kit (JDK) 25**
* **Apache Maven** instalado e configurado nas variáveis de ambiente (`PATH`).

### Como Rodar
1. Abra o terminal e navegue até a raiz do projeto (onde está o arquivo `pom.xml`).
2. Compile o projeto para baixar as dependências e gerar as classes:
   ```bash
   mvn clean compile
    ```
3. Rode o projeto com:
    ```bash
   mvn exec:java
    ```

# **Estruturas de Dados Utilizadas**
Implementamos as principais estruturas de dados do zero:

- Árvore B
    - Onde foi aplicada: No armazenamento e busca das receitas em disco (BTree.java, BTreeNode.java, DiskManager.java).

    - Por que foi utilizada: Ideal para indexação e leitura/escrita em sistemas de arquivos. Minimiza o número de acessos ao disco (I/O), mantendo os dados balanceados e permitindo buscar uma receita pelo ID de forma extremamente rápida, mesmo com grandes volumes de dados.


- Tabela Hash
    - Onde foi aplicada: No InvestigationService para o sistema de validação e segurança. É utilizada para armazenar os hashes de integridade das receitas (integrityTable), detectar receitas duplicadas (seen) e encontrar conflitos de versão entre ingredientes (recipeContents).

    - Por que foi utilizada: Oferece complexidade de tempo média O(1) para inserção e busca. É a estrutura perfeita para checagens rápidas de existência e para validar a integridade dos dados sem precisar fazer varreduras lentas (buscas lineares) em listas.

- Trie
    - Onde foi aplicada: No sistema de busca rápida (QuickSearchService e RecipeBook), especificamente para a funcionalidade de busca por prefixo (ex: encontrar todas as receitas que começam com "Bolo").

    - Por que foi utilizada: A busca em uma Trie depende apenas do tamanho da string de consulta, tornando a pesquisa por prefixos de texto muito mais natural e eficiente do que varrer uma lista comparando substrings ou mesmo buscar em uma árvore de busca convencional.

- Algoritmo Guloso

    - Onde foi aplicado: No "Modo Chef" (ChefService.java), especificamente nas funcionalidades de recomendação inteligente de pratos (recommendBestRecipe e recommendTopRecipes).

    - Por que foi utilizado: O objetivo do Modo Chef é sugerir o melhor prato com base nas restrições momentâneas do cliente (tempo, custo e dificuldade de preparo). Após filtrar as receitas válidas, o algoritmo calcula um Score para cada uma (maximizando notas e número de pedidos, enquanto penaliza tempo, custo e dificuldade). A abordagem Gulosa foi escolhida porque faz a escolha localmente ótima: o algoritmo sempre seleciona a receita com a maior pontuação absoluta naquele instante para ser a grande recomendação do chef. Isso resolve o problema de forma extremamente rápida, em tempo O(N) ou O(N log N) na ordenação, evitando abordagens pesadas e desnecessárias para este escopo e entregando a melhor decisão de.

# **Bibliotecas Utilizadas**
Optamos por utilizar poucas bibliotecas prontas para manter o foco na implementação manual das estruturas de dados:

**Bibliotecas nativas do Java:** * java.util.List e java.util.ArrayList foram utilizadas como estruturas de apoio dinâmicas para agrupar resultados de buscas, filtrar recomendações do chef e gerenciar retornos de métodos.

java.io.* (como IOException, o uso de manipulação de disco em DiskManager) foi essencial para a manipulação dos arquivos binários na Árvore B.

**Gson** (com.google.code.gson): A única dependência externa principal (além do JUnit para testes). Foi utilizada exclusivamente na classe FileLoader para fazer o parsing (leitura e conversão) do arquivo pequenasReceitas.json para objetos Java durante a carga inicial do sistema.

# **Base de Dados**
A base de dados do sistema teve como ponto de partida a estrutura do RecipeIngredientsDataSet.

Entretanto, para se adequar ao escopo deste trabalho, nós modificamos e expandimos o formato. Os dados foram armazenados em JSON (pequenasReceitas.json) e enriquecidos com atributos fundamentais para as regras de negócio de filtragem e avaliação (ChefService), incluindo:

 - id: Identificador único numérico.
 - category: Categoria do prato.
 - prepTime: Tempo de preparo.
 - cost: Custo de produção.
 - difficulty: Nível de dificuldade da receita.
 - rating e orderCount: Avaliação e Número de Pedidos. Métricas utilizadas para calcular o "score" e fazer as recomendações dos melhores pratos.

## Recuperação P1

**Questao a ser recuperada:** A dupla escolheu recuperar a Questão 5 da prova teórica P1, realizando a implementação prática completa do Desafio C: Persistência em Disco da Árvore B. O objetivo foi eliminar a dependência de armazenamento puramente volátil (RAM) para a indexação das receitas, emulando fielmente o comportamento de páginas/blocos de um sistema de arquivos físico diretamente em um arquivo binário.

**Explicação Teórica e Arquitetural do Upgrade:** A Árvore B tradicional mantém seus nós e ponteiros encadeados na memória RAM. No upgrade arquitetural realizado nas classes BTree, BTreeNode e DiskManager, a árvore foi desacoplada da volatilidade da memória:

- Gerenciamento de Blocos em Disco (DiskManager.java): Implementa um canal de persistência direta utilizando paginação lógica de tamanho fixo em disco. O método allocateNode() simula a alocação de um bloco físico no arquivo de armazenamento, retornando um ponteiro de arquivo (long representando o deslocamento em bytes/offset).

- Estrutura do Nó (BTreeNode.java): Cada nó da Árvore B conhece sua própria posição física no arquivo (atributo selfPosition) e armazena arrays de posições de seus filhos (atributo childrenPositions) e das posições físicas dos registros das receitas (atributo recipePositions), atuando estritamente como uma página indexada.

- Busca e Inserção: Ao chamar o método search(int key), o sistema não varre uma árvore montada na memória RAM. A busca inicia-se na posição da raiz e, a cada descida de nível, o DiskManager.readNode(nodePosition, t) executa uma operação de leitura física direta no arquivo binário para carregar exclusivamente o nó necessário para aquela tomada de decisão.

**Passo a Passo para validação:**
Para comprovar o isolamento dos blocos e a leitura diretamente do arquivo binário sem reconstrução da Árvore na RAM a partir do JSON do dataset original, siga as instruções abaixo:

1. **Geração Inicial do Arquivo Binário:** Rode o sistema uma primeira vez utilizando mvn exec:java. Os arquivos binários de persistência serão gerados e populados no disco (db_recipes.bin e recipes.dat). Encerre a aplicação para limpar totalmente a memória RAM.

2. **Simulação de RAM Limpa (Isolamento do JSON):**
    - Abra a classe principal App.java.

    - Para provar o isolamento absoluto, comente a linha responsável por carregar os dados brutos do JSON.
        ```bash
        //recipeBook.initializeSystem(recipes);
        ```

3. **Execução do Teste:** Inicie o sistema novamente com mvn exec:java.

4. **Realização da Busca:** No menu interativo do sistema, selecione a opção de busca de receita por ID, ou selecione o Livro de Receitas.

5. **Comprovação:** O sistema exibirá os dados completos da receita com sucesso instantaneamente. Isso prova que a Árvore B navegou pelos offsets de bytes em disco, localizou o ponteiro do registro e buscou a receita de forma isolada, sem utilizar o arquivo pequenasReceitas.json.