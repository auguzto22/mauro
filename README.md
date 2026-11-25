# Quantum Learn

Aplicativo Android em português para estudar física quântica desde as operações básicas até uma introdução aos campos quânticos e à computação quântica. As lições, a correção, o laboratório e o progresso funcionam offline.

## Trilha

| Módulo | Lições | Conteúdo |
| --- | ---: | --- |
| Matemática básica | 6 | Operações, frações, potências, raízes, equações e notação científica |
| Matemática intermediária | 6 | Álgebra, funções, trigonometria, logaritmos, vetores e números complexos |
| Matemática avançada | 8 | Limites, derivadas, integrais, probabilidade, álgebra linear, matrizes, equações diferenciais e Fourier |
| Fundamentos de física | 8 | Movimento, forças, energia, momento, ondas, eletricidade, magnetismo e campos eletromagnéticos |
| Introdução à física quântica | 6 | Corpo negro, fótons e constante de Planck, efeito fotoelétrico, dualidade, dupla fenda e incerteza |
| Mecânica quântica | 9 | Função de onda, regra de Born, superposição, estados, operadores, autovalores, notação de Dirac e as duas formas da equação de Schrödinger |
| Física quântica avançada | 9 | Tunelamento, spin, emaranhamento, decoerência, oscilador, hidrogênio, perturbação, campos quânticos e computação quântica |

As 52 lições têm objetivo, explicação, exemplo, diagrama, fórmula com seus símbolos, aprofundamento, resumo e exercícios. As referências em cada lição abrem no navegador. O conteúdo didático e as ilustrações são originais. As fontes incluem [OpenStax](https://openstax.org/subjects/science), [MIT OpenCourseWare](https://ocw.mit.edu/courses/8-04-quantum-physics-i-spring-2016/), [NIST](https://physics.nist.gov/cuu/Constants/) e [The Feynman Lectures on Physics](https://www.feynmanlectures.caltech.edu/III_01.html).

## Executar

Requer JDK 21, SDK Android 35 e ferramentas de compilação 35.0.0. A versão mínima de execução é Android 6.0, API 23. O Gradle Wrapper baixa e verifica a distribuição do Gradle. A primeira compilação precisa de acesso aos repositórios Maven Central, Gradle Plugin Portal e Google Maven.

```sh
./gradlew :core:test :app:lintDebug :app:assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

No Android Studio, abra a pasta do projeto, escolha um emulador ou dispositivo e execute a configuração do módulo `app`.

## Arquitetura

- `:core` é Kotlin/JVM sem dependência do Android. Contém o currículo, banco de questões, corretor, avaliador de expressões, motor adaptativo, provedor de tutor e modelos das simulações.
- `:data` usa Room com KSP para registrar respostas, conclusões e preferências. O esquema é versionado em `data/schemas`. UUIDs e transações tornam reenvios idempotentes e mantêm recompensas por pergunta consistentes no aparelho.
- `:app` usa Compose, Material 3, `ViewModel`, corrotinas e `Flow`. Desenhos nativos em Canvas e um renderizador do subconjunto de LaTeX usado pelo currículo mantêm as telas offline sem WebView. O tema pode ser claro, escuro ou seguir o aparelho.

## Aprendizado e correção

- Cada opção incorreta tem diagnóstico próprio. O retorno mostra a escolha, o conceito que pode ter sido confundido, o resultado esperado, a resolução e um caso distinto. Quando há uma questão correspondente disponível, o erro dispara uma verificação do mesmo assunto.
- O avaliador calcula expressões numéricas fechadas com precedência, potências, frações, raízes, constantes físicas, notação científica, conversões de unidades cadastradas e tolerância por pergunta. Respostas escritas são aceitas quando correspondem a formulações aprovadas. Outros casos pedem esclarecimento e permitem pular sem contabilizar erro.
- O domínio considera as últimas evidências por enunciado, ponderadas por dificuldade, e acertos em sessões espaçadas. A revisão inicia após um dia e pode progredir para intervalos de 3, 7, 14 e 30 dias. Concluir uma lição requer dois acertos em enunciados diferentes. Toda a trilha permanece livre para explorar.
- Os desafios oferecem seleção aleatória, revisão, prova, infinito, cálculo e desafio diário. Provas mostram correções no resultado. Geradores determinísticos produzem variações numéricas e completamento de equações.
- O Professor offline responde a ações sobre o assunto selecionado e ao último erro registrado. `TutorProvider` permite adicionar outros provedores. As explicações distinguem o modelo e suas condições, previsões observáveis e questões de interpretação.
- O Laboratório inclui dupla fenda, interferência, distribuição normal, função de onda, tunelamento, caixa infinita, oscilador harmônico, spin e esfera de Bloch. Cada visualização informa a aproximação utilizada.
- Em Perfil, o estudante pode acompanhar os últimos sete dias, XP, meta, sequência, domínio por assunto e revisões. A transferência manual salva um arquivo JSON pelo seletor de documentos do Android e mescla tentativas e conclusões por identificador. Preferências pertencem ao aparelho. A aplicação funciona sem conta, servidor ou API de IA.

## Verificação

A suíte JVM valida todos os enunciados cadastrados e suas soluções, as explicações dos distratores, a geração determinística, as conversões, o cálculo de progresso, a seleção adaptativa, a retenção e propriedades dos modelos físicos. O lint Android verifica os módulos que formam o APK.

Este conteúdo cobre o roteiro solicitado em lições introdutórias e progressivas. A conclusão interna indica acertos nas lições publicadas do módulo.
