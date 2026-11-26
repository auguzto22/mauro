package dev.quantumlearn.core

/*
 * Inglês 4: palavras de ligação e o apóstrofo.
 * Cada palavra tem significado, exemplo, tradução e pronúncia aproximada. As questões mostram a tradução
 * e pedem a palavra que dá aquele sentido, então só uma alternativa combina com o significado.
 */

private fun w(word: String, meaning: String, example: String, translation: String, sounds: String, vararg traps: String) =
    Word(word, meaning, example, translation, sounds, traps.map { it to "" })

private fun w(word: String, meaning: String, example: String, translation: String, sounds: String, traps: List<Pair<String, String>>) =
    Word(word, meaning, example, translation, sounds, traps)

internal val timeWords = listOf(
    w("just", "acabou de; só, apenas", "I just arrived.", "Eu acabei de chegar.", "djâst", "yet", "ever"),
    w("already", "já (antes do esperado)", "I have already eaten.", "Eu já comi.", "ólrédi", "yet", "still"),
    w("yet", "ainda (na negativa) ou já (na pergunta), no fim da frase", "I haven't finished yet.", "Ainda não terminei.", "iét", "already", "ever"),
    w("still", "ainda, continua", "She still lives with her parents.", "Ela ainda mora com os pais.", "stíl", "yet", "ever"),
    w("ever", "alguma vez, já (em perguntas)", "Have you ever been to Rio?", "Você já foi ao Rio alguma vez?", "évar", "yet", "still"),
    w("never", "nunca", "I never drink coffee.", "Eu nunca bebo café.", "névar", "ever", "yet"),
    w("again", "de novo", "Can you say that again?", "Pode dizer isso de novo?", "aguén", "already", "still"),
    w("soon", "logo, em breve", "See you soon!", "Até logo!", "sún", "already", "yet"),
    w("anymore", "não mais (com negativa)", "I don't play games anymore.", "Eu não jogo mais.", "énimór", "already", "just"),
    w("recently", "recentemente", "I recently started a new job.", "Comecei um emprego novo recentemente.", "ríssentli", "yet", "anymore")
)

internal val sequenceWords = listOf(
    w("first", "primeiro", "First, open the app. Then, log in.", "Primeiro, abra o app. Depois, entre na conta.", "fârst", "finally", "until"),
    w("then", "depois, então", "Wash the rice, then cook it.", "Lave o arroz, depois cozinhe.", "dén", "before", "until"),
    w("next", "em seguida, o próximo", "What do we do next?", "O que fazemos em seguida?", "nékst", "until", "since"),
    w("after", "depois de", "Let's talk after lunch.", "Vamos conversar depois do almoço.", "éftar", "since", "then"),
    w("before", "antes de", "Wash your hands before eating.", "Lave as mãos antes de comer.", "bifór", "then", "since"),
    w("finally", "finalmente, por fim", "After two hours, the bus finally arrived.", "Depois de duas horas, o ônibus finalmente chegou.", "fáinali", "until", "since"),
    w("until", "até (que)", "Wait here until I come back.", "Espere aqui até eu voltar.", "antíl", "since", "then"),
    w("since", "desde", "I have lived here since 2020.", "Moro aqui desde 2020.", "síns", "until", "after"),
    w("as soon as", "assim que", "Call me as soon as you arrive.", "Me liga assim que chegar.", "éz sún éz", "since", "until"),
    w("meanwhile", "enquanto isso", "I'll cook. Meanwhile, you set the table.", "Eu cozinho. Enquanto isso, você põe a mesa.", "mínuail", "since", "until")
)

internal val linkerWords = listOf(
    w("and", "e", "I like rice and beans.", "Eu gosto de arroz e feijão.", "énd", "but", "so"),
    w("but", "mas", "I studied a lot, but I failed the test.", "Estudei muito, mas fui mal na prova.", "bât", "so", "because"),
    w("or", "ou", "Do you want coffee or tea?", "Você quer café ou chá?", "ór", "but", "so"),
    w("so", "então, por isso", "It was raining, so I stayed home.", "Estava chovendo, então fiquei em casa.", "sôu", "but", "because"),
    w("because", "porque", "I'm hungry because I didn't eat.", "Estou com fome porque não comi.", "bicóz", "so", "but"),
    w("although", "embora", "Although it was late, we kept working.", "Embora fosse tarde, continuamos trabalhando.", "óldôu", "because", "so"),
    w("though", "mas, porém (no fim da frase)", "The food was good. Expensive, though.", "A comida era boa. Cara, porém.", "dôu", "because", "so"),
    w("however", "no entanto", "The plan is good. However, it's expensive.", "O plano é bom. No entanto, é caro.", "rrauévar", "because", "so"),
    w("even though", "mesmo que, apesar de", "She went to work even though she was sick.", "Ela foi trabalhar mesmo estando doente.", "íven dôu", "so", "or"),
    w("while", "enquanto", "I listen to music while I work.", "Eu ouço música enquanto trabalho.", "uáil", "so", "but"),
    w("unless", "a menos que", "We'll go unless it rains.", "Vamos, a menos que chova.", "anlés", "because", "so"),
    w("if", "se", "If you need help, call me.", "Se precisar de ajuda, me liga.", "if", "so", "but")
)

internal val additionWords = listOf(
    w("also", "também (no meio da frase)", "I also like jazz.", "Eu também gosto de jazz.", "ólsou", "either", "else"),
    w("too", "também (no fim da frase); demais", "I love you too.", "Eu também te amo.", "tú", "either", "else"),
    w("either", "também não (no fim de frase negativa)", "I don't like it either.", "Eu também não gosto.", "ídar", "too", "also"),
    w("even", "até, mesmo", "Everyone came, even my grandma.", "Todo mundo veio, até a minha avó.", "íven", "only", "either"),
    w("only", "só, apenas", "There is only one cookie left.", "Só sobrou um biscoito.", "ôunli", "either", "else"),
    w("besides", "além disso", "It's late. Besides, I'm tired.", "Está tarde. Além disso, estou cansado.", "bissáidz", "instead", "either"),
    w("instead", "em vez disso", "I didn't take the bus. I walked instead.", "Não peguei o ônibus. Fui a pé em vez disso.", "instéd", "either", "besides"),
    w("both", "ambos, os dois", "Both of my brothers live in Recife.", "Os meus dois irmãos moram em Recife.", "bôuth", "either", "else"),
    w("neither", "nenhum dos dois", "Neither answer is correct.", "Nenhuma das duas respostas está certa.", "nídar", "both", "too"),
    w("else", "mais, outro (anything else, someone else)", "Do you need anything else?", "Você precisa de mais alguma coisa?", "éls", "besides", "even")
)

internal val fillerWords = listOf(
    w("actually", "na verdade (não é \"atualmente\")", "Actually, I'm not from here.", "Na verdade, não sou daqui.", "ékchuali", "anyway", "maybe"),
    w("really", "muito; de verdade", "This song is really good.", "Essa música é muito boa.", "ríli", "kind of", "almost"),
    w("basically", "basicamente, resumindo", "Basically, we need more time.", "Basicamente, precisamos de mais tempo.", "bêissikli", "actually", "anyway"),
    w("anyway", "enfim, de qualquer forma", "Anyway, let's go home.", "Enfim, vamos para casa.", "éniuei", "actually", "basically"),
    w("maybe", "talvez", "Maybe it will rain later.", "Talvez chova mais tarde.", "mêibi", "probably", "really"),
    w("probably", "provavelmente", "She's probably at work.", "Ela provavelmente está no trabalho.", "próbabli", "really", "almost"),
    w("kind of", "meio, um pouco", "I'm kind of tired.", "Estou meio cansado.", "káind óv", "really", "enough"),
    w("I mean", "quer dizer", "It's far. I mean, it's two hours by bus.", "É longe. Quer dizer, são duas horas de ônibus.", "ai mín", "you know", "anyway"),
    w("you know", "sabe? (para manter a conversa)", "He's, you know, a little shy.", "Ele é, sabe, um pouco tímido.", "iú nôu", "I mean", "anyway"),
    w("like", "tipo (na fala informal)", "It was, like, really expensive.", "Foi, tipo, muito caro.", "láik", "enough", "especially"),
    w("pretty", "bem, bastante (antes de adjetivo)", "The test was pretty easy.", "A prova foi bem fácil.", "príti", "enough", "almost"),
    w("quite", "bastante", "It's quite cold today.", "Está bastante frio hoje.", "kuáit", "almost", "especially"),
    w("almost", "quase", "I almost forgot!", "Eu quase esqueci!", "ólmoust", "enough", "especially"),
    w("enough", "suficiente, o bastante", "Do we have enough money?", "Temos dinheiro suficiente?", "ináf", "almost", "quite"),
    w("especially", "especialmente", "I love fruit, especially mango.", "Amo frutas, especialmente manga.", "ispéxali", "almost", "enough")
)

internal val apostropheWords = listOf(
    w("I'm", "I am, eu sou/estou", "I'm hungry.", "Estou com fome.", "áim", listOf(
        "Im" to "Sem o apóstrofo a palavra fica errada. Ele marca a letra que saiu: o a de am.",
        "I'am" to "O apóstrofo substitui o a. Não sobra a nenhum: I'm.")),
    w("don't", "do not, não (com verbo de ação)", "I don't know.", "Eu não sei.", "dônt", listOf(
        "dont" to "Sem o apóstrofo está errado. Ele ocupa o lugar do o de not.",
        "do'nt" to "O apóstrofo vai no lugar da letra que saiu, o o de not: don't.")),
    w("can't", "cannot, não pode/consegue", "I can't swim.", "Eu não sei nadar.", "ként", listOf(
        "cant" to "Sem o apóstrofo está errado.",
        "ca'nt" to "O apóstrofo fica onde saíram as letras: can't.")),
    w("won't", "will not, não vai", "I won't forget.", "Eu não vou esquecer.", "uônt", listOf(
        "willn't" to "Will not tem contração irregular: won't.",
        "wont" to "Sem o apóstrofo vira outra palavra (wont = costume).")),
    w("it's", "it is ou it has", "It's raining.", "Está chovendo.", "its", listOf(
        "its" to "Its sem apóstrofo é possessivo (dele, de coisa). Aqui é it is.",
        "its'" to "Essa forma não existe.")),
    w("its", "dele ou dela (de coisa ou animal), sem apóstrofo", "The dog wagged its tail.", "O cachorro abanou o rabo (dele).", "its", listOf(
        "it's" to "It's = it is. Para posse de coisa, its não tem apóstrofo.",
        "its'" to "Essa forma não existe.")),
    w("you're", "you are, você é/está", "You're my best friend.", "Você é o meu melhor amigo.", "iór", listOf(
        "your" to "Your é possessivo (seu). Aqui é you are.",
        "youre" to "Sem o apóstrofo está errado.")),
    w("they're", "they are, eles são/estão", "They're at school.", "Eles estão na escola.", "dér", listOf(
        "their" to "Their é possessivo (deles).", "there" to "There é lá, ou there is.")),
    w("their", "deles ou delas, sem apóstrofo", "They love their dog.", "Eles amam o cachorro deles.", "dér", listOf(
        "they're" to "They're = they are.", "there" to "There é lá.")),
    w("let's", "let us, vamos", "Let's go!", "Vamos!", "léts", listOf(
        "lets" to "Lets sem apóstrofo é o verbo deixar (he lets). Para \"vamos\", use let's.",
        "let'us" to "A contração de let us é let's.")),
    w("Pedro's", "do Pedro ('s indica dono)", "This is Pedro's car.", "Este é o carro do Pedro.", "pêdrouz", listOf(
        "Pedros" to "Sem apóstrofo vira plural, vários Pedros.", "Pedro is" to "Aqui o 's é de posse, não de is.")),
    w("girls'", "das meninas (plural com s: o apóstrofo vem depois)", "The girls' room is upstairs.", "O quarto das meninas fica lá em cima.", "gârlz", listOf(
        "girl's" to "Girl's é de uma menina só.", "girls" to "Sem apóstrofo não indica posse.")),
    w("I'd", "I would (gostaria) ou I had", "I'd like a coffee, please.", "Eu gostaria de um café, por favor.", "áid", listOf(
        "I'll" to "I'll = I will, vou.", "Id" to "Sem o apóstrofo está errado.")),
    w("I've", "I have", "I've finished my homework.", "Eu terminei a lição de casa.", "áiv", listOf(
        "I'm" to "I'm = I am.", "Iv'e" to "O apóstrofo fica no lugar do ha de have: I've."))
)

/** Todas as palavras, para explicar armadilhas e para validação. */
internal val glossary by lazy {
    (timeWords + sequenceWords + linkerWords + additionWords + fillerWords + apostropheWords).associateBy { it.word.lowercase() }
}

private fun boundary(word: String) = Regex("(?<![\\w'])" + Regex.escape(word) + "(?![\\w'])", RegexOption.IGNORE_CASE)

/** Questão de lacuna com a tradução como guia: só a palavra com aquele sentido serve. */
internal fun wordPick(entry: Word): Pick {
    val match = requireNotNull(boundary(entry.word).find(entry.example)) { "Exemplo sem a palavra ${entry.word}" }
    val answer = match.value
    val capital = answer.first().isUpperCase() && !entry.word.first().isUpperCase()
    fun shape(text: String) = if (capital) text.replaceFirstChar(Char::uppercase) else text
    val traps = entry.traps.map { (trap, why) ->
        shape(trap) to why.ifBlank {
            val other = glossary[trap.lowercase()]
            if (other != null) "${other.word} = ${other.meaning}. A tradução pede ${entry.meaning}." else "Não combina com a tradução."
        }
    }
    return Pick("Complete conforme a tradução: \"${entry.translation}\"", entry.example.replaceRange(match.range, "___"), answer,
        "${entry.word} = ${entry.meaning}.", traps,
        listOf("Ache na tradução a ideia que falta: ${entry.meaning}.", "${entry.meaning.replaceFirstChar(Char::uppercase)} → ${entry.word}."))
}

private fun wordGuide(
    id: String, latex: String, text: String, reading: String, symbols: List<FormulaSymbol>, build: List<FormulaStep>,
    words: List<Word>, scenes: List<Triple<Scene, String, String>>, isolation: Isolation
): FormulaGuide {
    val cases = scenes.map { (scene, place, word) ->
        val entry = words.first { it.word == word }
        RealCase(scene, place, "${entry.word.replaceFirstChar(Char::uppercase)} = ${entry.meaning}. ${entry.translation}", entry.example,
            pick = wordPick(entry))
    }
    val phrases = words.take(5).map { Phrase(it.example, it.translation, "${it.word}: ${it.sounds}") }
    return FormulaGuide(id, latex, text, reading, symbols, build, cases, isolation, null,
        { a, b -> wordPick(words[((a - 2) * 8 + (b - 2)) % words.size]) }, phrases, words)
}

internal fun wordGuides(): List<FormulaGuide> = listOf(
    wordGuide("en-just", """I\ just\ arrived.\quad Not\ yet.\quad Still\ here.""", "just · already · yet · still · ever · never…",
        "Palavras pequenas que dizem quando: acabou de, já, ainda, alguma vez, nunca.",
        listOf(sym("just", "acabou de, agora mesmo"), sym("already", "já, antes do esperado"), sym("yet", "ainda, no fim de negativas"),
            sym("still", "ainda, algo que continua"), sym("ever", "alguma vez, em perguntas")),
        listOf(
            step("""I\ just\ ate.""", "Just fica antes do verbo: acabei de comer."),
            step("""I\ have\ already\ eaten.""", "Already também fica no meio: já comi."),
            step("""I\ haven't\ eaten\ yet.""", "Yet vai no fim, em negativas e perguntas."),
            step("""I'm\ still\ hungry.""", "Still mostra que algo continua: ainda estou com fome.")
        ),
        timeWords, listOf(Triple(Scene.Phone, "Mensagem rápida", "just"), Triple(Scene.Work, "Prazo no trabalho", "yet"),
            Triple(Scene.Travel, "Conversa de viagem", "ever")),
        iso("Como dizer \"Ainda não terminei\"?", "I haven't finished yet.",
            "Ainda não, no fim de uma negativa, é yet.",
            "I haven't finished still." to "Still é ainda no sentido de continuar, não em \"ainda não\" no fim da frase.",
            "I already haven't finished." to "Already é já, o contrário do que a frase quer dizer.")
    ),
    wordGuide("en-sequence", """First…\ then…\ finally""", "first · then · next · after · before · finally · until · since…",
        "Palavras que colocam as coisas em ordem: primeiro, depois, antes, até, desde.",
        listOf(sym("first", "primeiro"), sym("then", "depois"), sym("before", "antes de"), sym("until", "até"), sym("since", "desde")),
        listOf(
            step("""First,\ open\ the\ app.""", "Comece a sequência com first."),
            step("""Then,\ log\ in.""", "Continue com then ou next."),
            step("""Finally,\ click\ save.""", "Feche com finally."),
            step("""since\ 2020\quad until\ 6\ pm""", "Since marca o começo e until marca o fim.")
        ),
        sequenceWords, listOf(Triple(Scene.Computer, "Tutorial de app", "first"), Triple(Scene.Phone, "Combinando encontro", "as soon as"),
            Triple(Scene.Home, "Receita", "then")),
        iso("\"Moro aqui desde 2020\" em inglês:", "I have lived here since 2020.",
            "Desde = since, com have lived.",
            "I live here until 2020." to "Until é até, o fim de um período.",
            "I live here after 2020." to "After é depois de.")
    ),
    wordGuide("en-linkers", """frase\ +\ but/so/because\ +\ frase""", "and · but · or · so · because · although · however · if…",
        "Conectivos ligam duas ideias e dizem a relação entre elas: soma, contraste, causa, consequência ou condição.",
        listOf(sym("and", "e, soma"), sym("but", "mas, contraste"), sym("so", "então, consequência"), sym("because", "porque, causa"),
            sym("although", "embora, contraste no começo"), sym("if", "se, condição")),
        listOf(
            step("""It\ rained,\ so\ I\ stayed\ home.""", "So: o que veio depois é a consequência."),
            step("""I\ stayed\ home\ because\ it\ rained.""", "Because: o que vem depois é a causa."),
            step("""It\ rained,\ but\ I\ went\ out.""", "But: o resultado foi contra o esperado."),
            step("""Although\ it\ rained,\ I\ went\ out.""", "Although faz o mesmo contraste, começando a frase.")
        ),
        linkerWords, listOf(Triple(Scene.Talk, "Explicando um atraso", "because"), Triple(Scene.Work, "Opinião numa reunião", "however"),
            Triple(Scene.Home, "Mudança de planos", "so")),
        iso("Qual conectivo indica a CAUSA?", "because",
            "Because responde por quê: a causa vem logo depois dele.",
            "so" to "So introduz a consequência, não a causa.",
            "but" to "But introduz um contraste.")
    ),
    wordGuide("en-addition", """I\ like\ it\ too.\quad I\ don't\ like\ it\ either.""", "also · too · either · even · only · besides · instead…",
        "Palavras que somam, restringem ou trocam ideias: também, também não, até, só, em vez de.",
        listOf(sym("also", "também, no meio da frase"), sym("too", "também, no fim da frase"), sym("either", "também não"),
            sym("only", "só, apenas"), sym("instead", "em vez disso")),
        listOf(
            step("""I\ also\ like\ jazz.""", "Also fica antes do verbo principal."),
            step("""I\ like\ jazz\ too.""", "Too fica no fim da frase afirmativa."),
            step("""I\ don't\ like\ it\ either.""", "Na negativa, \"também\" vira either."),
            step("""I\ walked\ instead.""", "Instead troca uma opção por outra.")
        ),
        additionWords, listOf(Triple(Scene.Talk, "Concordando com alguém", "either"), Triple(Scene.Money, "No caixa", "else"),
            Triple(Scene.Transport, "Mudança de trajeto", "instead")),
        iso("Alguém diz \"I don't like it.\" Como dizer \"Eu também não\"?", "I don't like it either.",
            "Também na negativa é either.",
            "I don't like it too." to "Too é também em frases afirmativas.",
            "I also don't like it neither." to "Negação dupla: em inglês use só uma.")
    ),
    wordGuide("en-fillers", """Actually,\ it's\ kind\ of\ far.""", "actually · really · anyway · maybe · kind of · I mean · pretty…",
        "Palavras que deixam a fala natural: na verdade, muito, enfim, talvez, meio, quer dizer.",
        listOf(sym("actually", "na verdade"), sym("anyway", "enfim, voltando ao assunto"), sym("kind of", "meio, um pouco"),
            sym("pretty", "bem, bastante"), sym("almost", "quase")),
        listOf(
            step("""Actually\ ≠\ atualmente""", "Falso amigo: actually é na verdade. Atualmente é currently."),
            step("""pretty\ good,\ kind\ of\ tired""", "Pretty aumenta, kind of suaviza."),
            step("""I\ mean,\ …""", "I mean corrige ou explica o que você acabou de dizer."),
            step("""Anyway,\ …""", "Anyway encerra um desvio e volta ao assunto.")
        ),
        fillerWords, listOf(Triple(Scene.Talk, "Corrigindo uma informação", "actually"), Triple(Scene.Games, "Comentando uma série", "pretty"),
            Triple(Scene.Phone, "Áudio para um amigo", "kind of")),
        iso("O que significa \"Actually, I live in Recife\"?", "Na verdade, eu moro em Recife.",
            "Actually é um falso amigo: significa na verdade.",
            "Atualmente, eu moro em Recife." to "Atualmente em inglês é currently ou nowadays.",
            "Talvez eu more em Recife." to "Talvez é maybe.")
    ),
    wordGuide("en-apostrophe", """I\ am\ →\ I'm\quad do\ not\ →\ don't\quad Pedro's\ car""", "I am → I'm · do not → don't · Pedro's car",
        "O apóstrofo (') é o sinalzinho no alto entre as letras. Ele faz duas coisas: junta palavras (I'm) e mostra o dono (Pedro's).",
        listOf(sym("'", "o apóstrofo, que marca letras que saíram ou posse"), sym("n't", "not encurtado"),
            sym("'s", "is, has ou posse de alguém"), sym("'re", "are encurtado"), sym("s'", "posse de um plural terminado em s")),
        listOf(
            step("""I\ am\ →\ I'm""", "O a de am sai. O apóstrofo fica no lugar dele."),
            step("""do\ not\ →\ don't""", "O o de not sai. O apóstrofo fica entre n e t."),
            step("""Pedro's\ car""", "Com nome de pessoa, 's mostra o dono: o carro do Pedro."),
            step("""it's\ (it\ is)\ ≠\ its\ (dele)""", "Pegadinha: possessivos (its, your, their) nunca têm apóstrofo.")
        ),
        apostropheWords, listOf(Triple(Scene.Phone, "Mensagem de texto", "I'm"), Triple(Scene.Home, "Falando de família", "Pedro's"),
            Triple(Scene.Talk, "Convite", "let's")),
        iso("Qual frase usa o apóstrofo certo?", "It's cold, and the cat hid its nose.",
            "It's = it is. Its (dele, de coisa) não tem apóstrofo.",
            "Its cold, and the cat hid it's nose." to "Ficou trocado: it's é it is e its é posse.",
            "It's cold, and the cat hid it's nose." to "O segundo its é posse, então não leva apóstrofo.")
    )
)

internal fun Curriculum.englishJust() = lesson(
    "en-just", "Just, already, yet, still e outras de tempo", "Use as palavrinhas de tempo que o inglês fala o tempo todo.",
    "Just, already, yet, still, ever e never mudam o sentido da frase sem mudar o verbo. Elas dizem se algo acabou de acontecer, se já aconteceu, se ainda não aconteceu ou se continua.",
    "I just arrived (acabei de chegar). I haven't eaten yet (ainda não comi). She still works here (ela ainda trabalha aqui).",
    """just\ ·\ already\ ·\ yet\ ·\ still\ ·\ ever\ ·\ never""",
    "just = acabou de. already = já. yet = ainda (negativa) ou já (pergunta), no fim. still = ainda, continua. ever = alguma vez. never = nunca.",
    "Posição: just, already, still, ever e never ficam antes do verbo principal (I just left, I have already seen it). Yet fica no fim (Not yet!). Com have + particípio, a palavra vai entre os dois: I have just finished.",
    Diagram.Sentence, "A palavra de tempo entra como um bloco pequeno perto do verbo.",
    "Acabou de: just. Já: already. Ainda não: not yet. Ainda continua: still.",
    "Mensagens do dia a dia: I just woke up. Are you home yet? I'm still at work.",
    listOf(english, cambridge), listOf("en-past"),
    choose("Complete (\"Ainda não terminei\"):\nI haven't finished ___.", 1, listOf(
        wrong("already", "Already é já, o contrário de ainda não.", "Already e yet"),
        correct("yet", "Ainda não, no fim da negativa: yet."),
        wrong("still", "Still é ainda no sentido de continuar.", "Still e yet")
    ), "A frase é negativa e a ideia é \"ainda não\".", "No fim da negativa: yet.", "Not yet! = Ainda não!"),
    choose("Complete (\"Acabei de acordar\"):\nI ___ woke up.", 0, listOf(
        correct("just", "Acabou de: just."),
        wrong("ever", "Ever é alguma vez.", "Just e ever"),
        wrong("yet", "Yet vai no fim, em negativas e perguntas.", "Posição do yet")
    ), "Acabei de = algo muito recente.", "Recente → just.", "She just left. = Ela acabou de sair.")
)

internal fun Curriculum.englishSequence() = lesson(
    "en-sequence", "First, then, before, until, since", "Conte coisas em ordem e marque começo e fim.",
    "Para explicar passos, contar uma história ou marcar horários, o inglês usa palavras de sequência e de limite de tempo.",
    "First, I wake up. Then, I take a shower. After breakfast, I go to work. I work until 6 pm.",
    """First\ →\ Then\ →\ Finally""",
    "first = primeiro. then/next = depois, em seguida. finally = por fim. before/after = antes/depois de. until = até. since = desde. as soon as = assim que.",
    "Depois de before e after, use substantivo ou verbo com -ing: before eating, after lunch. Since aparece com have + particípio: I have lived here since 2020.",
    Diagram.Sentence, "Blocos em fila, um depois do outro, como os passos de uma receita.",
    "Ordem: first, then, finally. Limite: since (desde) e until (até).",
    "Para explicar como usar um app, passar uma receita ou contar o seu dia.",
    listOf(english, cambridge), listOf("en-just"),
    choose("Complete (\"Espere aqui até eu voltar\"):\nWait here ___ I come back.", 2, listOf(
        wrong("since", "Since é desde.", "Since e until"),
        wrong("then", "Then é depois.", "Sequência e limite"),
        correct("until", "Até = until.")
    ), "A ideia é um limite final: até.", "Até → until.", "The store is open until 10 pm."),
    choose("Complete (\"Lave as mãos antes de comer\"):\nWash your hands ___ eating.", 0, listOf(
        correct("before", "Antes de = before."),
        wrong("after", "After é depois de.", "Before e after"),
        wrong("since", "Since é desde.", "Since e before")
    ), "A ideia é antes.", "Antes de → before.", "Brush your teeth after eating.")
)

internal fun Curriculum.englishLinkers() = lesson(
    "en-linkers", "And, but, so, because, although", "Ligue ideias com o conectivo certo.",
    "Conectivos são as pontes entre as ideias. Cada um diz uma relação: and soma, but contrasta, so dá a consequência, because dá a causa, if põe uma condição.",
    "I was tired, so I slept early (consequência). I slept early because I was tired (causa). I was tired, but I studied (contraste).",
    """ideia\ +\ conectivo\ +\ ideia""",
    "and = e. but = mas. or = ou. so = então. because = porque. although/even though = embora, mesmo que. however = no entanto. while = enquanto. unless = a menos que. if = se.",
    "However costuma começar a frase, seguido de vírgula. Although e even though ligam duas partes na mesma frase. Though no fim da frase é informal: It's expensive, though.",
    Diagram.Sentence, "Duas ideias e um bloco no meio que diz a relação entre elas.",
    "Causa: because. Consequência: so. Contraste: but, although, however. Condição: if, unless.",
    "Para explicar algo no trabalho, dar opinião e contar histórias com lógica.",
    listOf(english, cambridge), listOf("en-present"),
    choose("Complete (\"Estava chovendo, então fiquei em casa\"):\nIt was raining, ___ I stayed home.", 1, listOf(
        wrong("because", "Because introduz a causa. Aqui vem a consequência.", "Causa e consequência"),
        correct("so", "Consequência: so."),
        wrong("but", "But é contraste.", "Contraste e consequência")
    ), "Ficar em casa é a consequência da chuva.", "Consequência → so.", "I'm tired, so I'll go to bed."),
    choose("Complete (\"Embora fosse caro, eu comprei\"):\n___ it was expensive, I bought it.", 0, listOf(
        correct("Although", "Embora = although."),
        wrong("Because", "Because é porque.", "Contraste e causa"),
        wrong("So", "So é então.", "Contraste e consequência")
    ), "A ideia é contraste: embora.", "Embora → although.", "Although he's young, he's very wise.")
)

internal fun Curriculum.englishAddition() = lesson(
    "en-addition", "Also, too, either, even, only, instead", "Some, restrinja e troque ideias.",
    "Também tem três formas em inglês: also (no meio), too (no fim) e either (no fim de negativas). Even, only e instead ajustam o sentido da frase.",
    "I like pizza too. I also like burgers. I don't like fish either. I only eat chicken.",
    """also\ (meio)\quad too\ (fim)\quad either\ (negativa)""",
    "also = também, no meio. too = também, no fim. either = também não. even = até. only = só. besides = além disso. instead = em vez disso. both = os dois. neither = nenhum dos dois. else = mais, outro.",
    "Too também significa demais antes de adjetivo: too hot (quente demais). Me too e me neither são respostas rápidas: I'm hungry. Me too!",
    Diagram.Sentence, "O bloco extra muda de lugar conforme a palavra: meio ou fim da frase.",
    "Também: also, too. Também não: either. Só: only. Em vez: instead.",
    "Para concordar numa conversa (me too, me neither) e pedir algo no caixa (anything else?).",
    listOf(english, cambridge), listOf("en-linkers"),
    choose("Complete (\"Eu também não gosto\"):\nI don't like it ___.", 2, listOf(
        wrong("too", "Too é também em frases afirmativas.", "Too e either"),
        wrong("also", "Also fica no meio e em afirmativas.", "Also e either"),
        correct("either", "Também não: either.")
    ), "A frase é negativa.", "Também na negativa → either.", "She can't come either."),
    choose("Complete (\"Só sobrou um biscoito\"):\nThere is ___ one cookie left.", 0, listOf(
        correct("only", "Só = only."),
        wrong("even", "Even é até.", "Even e only"),
        wrong("else", "Else é mais, outro.", "Else e only")
    ), "A ideia é restrição: só.", "Só → only.", "I only have five minutes.")
)

internal fun Curriculum.englishFillers() = lesson(
    "en-fillers", "Actually, really, anyway, kind of", "Fale com o jeito natural de quem fala inglês.",
    "Nativos usam o tempo todo palavras que dão tom à fala: corrigir (actually), reforçar (really, pretty), suavizar (kind of), voltar ao assunto (anyway) e explicar (I mean).",
    "Actually, I'm kind of busy now. I mean, I can talk later. Anyway, it was really nice to see you!",
    """Actually,\ it's\ kind\ of\ late.""",
    "actually = na verdade. really = muito. basically = resumindo. anyway = enfim. maybe = talvez. probably = provavelmente. kind of = meio. I mean = quer dizer. you know = sabe. like = tipo. pretty = bem. quite = bastante. almost = quase. enough = suficiente. especially = especialmente.",
    "Actually é falso amigo: não é atualmente (currently). Enough vem depois do adjetivo (big enough) e antes do substantivo (enough money). Like como \"tipo\" é bem informal.",
    Diagram.Sentence, "Palavras-tempero: entram no começo ou antes do adjetivo e mudam o tom.",
    "Na verdade: actually. Muito: really. Meio: kind of. Enfim: anyway. Quase: almost.",
    "Em séries, vídeos e conversas, essas palavras aparecem em quase toda frase. Reconhecer ajuda a entender e a soar natural.",
    listOf(english, cambridge), listOf("en-linkers"),
    choose("O que significa \"Actually\"?", 1, listOf(
        wrong("Atualmente", "Falso amigo. Atualmente é currently.", "Falsos amigos"),
        correct("Na verdade", "Actually = na verdade, de fato."),
        wrong("Talvez", "Talvez é maybe.", "Actually e maybe")
    ), "Actually parece atualmente, mas é falso amigo.", "Actually = na verdade.", "Currently = atualmente."),
    choose("Complete (\"Temos dinheiro suficiente?\"):\nDo we have ___ money?", 2, listOf(
        wrong("almost", "Almost é quase.", "Almost e enough"),
        wrong("quite", "Quite é bastante, antes de adjetivo.", "Quite e enough"),
        correct("enough", "Suficiente = enough.")
    ), "A ideia é suficiente.", "Suficiente → enough, antes do substantivo.", "The room is big enough.")
)

internal fun Curriculum.englishApostrophe() = lesson(
    "en-apostrophe", "O apóstrofo ('): I'm, don't, Pedro's", "Saiba quando colocar o sinalzinho (') e quando não colocar.",
    "O inglês não tem acentos como os do português. O sinalzinho no alto que aparece entre as letras é o apóstrofo ('). Ele tem duas funções: juntar duas palavras numa só (contração) e mostrar de quem é algo (posse).",
    "I am → I'm. Do not → don't. It is → it's. O carro do Pedro → Pedro's car. O quarto das meninas → the girls' room.",
    """I\ am\ →\ I'm\quad Pedro's\ car""",
    "Contração: o apóstrofo ocupa o lugar das letras que saíram (do not → don't). Posse: nome + 's (Pedro's). Plural terminado em s: só o apóstrofo depois (the girls').",
    "Possessivos de pronome nunca têm apóstrofo: its, your, their, hers, ours. Por isso its (dele) ≠ it's (it is), your (seu) ≠ you're (you are), their (deles) ≠ they're (they are). Irregulares: will not → won't, can not → can't. No inglês, palavras como café podem aparecer com acento, mas são emprestadas de outras línguas.",
    Diagram.Sentence, "Dois blocos que se juntam num só, com o apóstrofo no ponto de encontro.",
    "Juntar palavras: o apóstrofo fica onde saíram letras. Dono: 's. Pronomes possessivos: sem apóstrofo.",
    "Em mensagens, legendas e e-mails, contrações estão em quase toda frase: I'm, don't, can't, it's, let's.",
    listOf(english, cambridge), listOf("en-to-be-questions", "en-possessives"),
    choose("Onde fica o apóstrofo em \"do not\" encurtado?", 1, listOf(
        wrong("do'nt", "O apóstrofo vai no lugar do o que saiu de not.", "Posição do apóstrofo"),
        correct("don't", "O o de not sai e o apóstrofo fica no lugar dele."),
        wrong("dont", "Sem o apóstrofo está errado.", "Apóstrofo obrigatório")
    ), "Do not: a letra que sai é o o de not.", "O apóstrofo ocupa o lugar dela: don't.", "Is not → isn't."),
    choose("Complete (\"O seu celular está tocando\"):\n___ phone is ringing.", 0, listOf(
        correct("Your", "Seu = your, possessivo, sem apóstrofo."),
        wrong("You're", "You're = you are. Aqui é posse.", "Your e you're"),
        wrong("Your's", "Possessivos não levam apóstrofo.", "Possessivo sem apóstrofo")
    ), "A ideia é posse: seu.", "Possessivo → your, sem apóstrofo.", "You're late. = Você está atrasado.")
)
