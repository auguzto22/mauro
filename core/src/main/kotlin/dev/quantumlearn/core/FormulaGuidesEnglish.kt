package dev.quantumlearn.core

/*
 * Guias da trilha de inglês. A "fórmula" de cada lição é a estrutura da frase.
 * As questões geradas são de lacuna, com uma armadilha típica explicada em cada alternativa errada.
 */

private fun english(
    id: String, latex: String, text: String, reading: String, symbols: List<FormulaSymbol>,
    build: List<FormulaStep>, cases: List<RealCase>, isolation: Isolation, phrases: List<Phrase>,
    pick: (Int, Int) -> Pick
) = FormulaGuide(id, latex, text, reading, symbols, build, cases, isolation, null, pick, phrases)

private fun say(english: String, portuguese: String, sounds: String) = Phrase(english, portuguese, sounds)

private fun fill(instruction: String, sentence: String, answer: String, why: String, steps: List<String>,
                 vararg traps: Pair<String, String>) =
    Pick(instruction, sentence, answer, why, traps.toList().filter { it.first != answer }.distinctBy { it.first }, steps)

private fun case(scene: Scene, place: String, story: String, example: String, pick: Pick) =
    RealCase(scene, place, story, example, pick = pick)

private fun String.cap() = replaceFirstChar(Char::uppercase)

/** Sujeito, pronome equivalente e as formas que mudam com ele. */
private data class Who(val subject: String, val pronoun: String, val be: String, val third: Boolean, val past: String)

private val people = listOf(
    Who("I", "I", "am", false, "was"), Who("You", "you", "are", false, "were"), Who("He", "he", "is", true, "was"),
    Who("She", "she", "is", true, "was"), Who("We", "we", "are", false, "were"), Who("They", "they", "are", false, "were"),
    Who("My mother", "she", "is", true, "was"), Who("The kids", "they", "are", false, "were"),
    Who("Carlos", "he", "is", true, "was"), Who("My friends", "they", "are", false, "were")
)

private fun who(a: Int, b: Int) = people[(a * 3 + b) % people.size]

/** O sujeito no meio da frase: nomes próprios e I continuam com maiúscula. */
private fun Who.inside() = if (subject == "I" || subject == "Carlos") subject else subject.replaceFirstChar(Char::lowercase)

/** "Carlos (he)" ou só "He" quando o sujeito já é o pronome. */
private fun Who.named() = if (subject.lowercase() == pronoun) subject else "$subject ($pronoun)"

/** Forma do presente com he, she, it. */
private fun third(base: String) = when {
    base == "have" -> "has"
    base.endsWith("y") && base.length > 2 && base[base.length - 2] !in "aeiou" -> base.dropLast(1) + "ies"
    base.endsWith("o") || base.endsWith("ch") || base.endsWith("sh") || base.endsWith("s") || base.endsWith("x") -> base + "es"
    else -> base + "s"
}

private fun beRule(who: Who) = when (who.be) {
    "am" -> "Com I, o to be é am."
    "is" -> "${who.subject} equivale a ${who.pronoun}, singular: is."
    else -> "${who.subject} equivale a ${who.pronoun}: are."
}

internal fun englishGuides(): List<FormulaGuide> = listOf(
    english("en-pronouns", """I\ \ you\ \ he\ \ she\ \ it\ \ we\ \ they""", "I · you · he · she · it · we · they",
        "Quem faz a ação: eu, você, ele, ela, coisa, nós, eles.",
        listOf(sym("I", "eu, sempre com letra maiúscula"), sym("you", "você ou vocês"), sym("he", "ele, um homem"),
            sym("she", "ela, uma mulher"), sym("it", "uma coisa ou um animal"), sym("we", "nós, incluindo quem fala"),
            sym("they", "eles ou elas, várias pessoas ou coisas")),
        listOf(
            step("""Ana\ is\ a\ teacher.""", "Primeiro aparece o nome."),
            step("""She\ lives\ in\ Recife.""", "Depois, o pronome substitui o nome para não repetir."),
            step("""(Eu)\ sou\ estudante\ →\ I\ am\ a\ student.""", "O português esconde o sujeito. O inglês sempre escreve."),
            step("""It\ is\ hot.""", "Até para falar do tempo existe sujeito: it.")
        ),
        listOf(
            case(Scene.Talk, "Falando de um amigo", "Você conta a alguém sobre o seu amigo João: diz o nome uma vez e depois usa he.",
                "João is my friend. He is funny.",
                fill("Complete:", "João is my friend. ___ works with me.", "He", "João é um homem: he.",
                    listOf("Quem trabalha com você? O João.", "Homem → he."),
                    "She" to "She é para mulher.", "It" to "It é para coisa ou animal.")),
            case(Scene.Phone, "Mensagem no grupo", "Num grupo, para falar de você e dos colegas juntos, use we.",
                "We are at the bar. Come!",
                fill("Complete (você e seus amigos):", "___ are at the restaurant. Come here!", "We", "Você está incluído no grupo: we.",
                    listOf("Quem está no restaurante? Você e os amigos.", "Grupo com você → we."),
                    "They" to "They não inclui você.", "I" to "I é só você."))
        ),
        iso("Como dizer \"Está chovendo\"?", "It is raining.",
            "Em inglês a frase precisa de sujeito. Para o tempo, o sujeito é it.",
            "Is raining." to "Falta o sujeito. O inglês não aceita frase sem ele.",
            "He is raining." to "He é uma pessoa. Clima usa it."),
        listOf(say("I am from Brazil.", "Eu sou do Brasil.", "ai ém fróm brazíl"),
            say("She is my sister.", "Ela é minha irmã.", "xí iz mai sístar"),
            say("They are my friends.", "Eles são meus amigos.", "dêi ár mai fréndz"),
            say("It is cold today.", "Está frio hoje.", "it iz côuld tudêi"),
            say("We are late!", "Estamos atrasados!", "uí ár lêit"))
    ) { a, b ->
        val nouns = listOf("Maria" to "She", "Pedro" to "He", "Maria and I" to "We", "The car" to "It",
            "My parents" to "They", "The dog" to "It", "Lucas and Bia" to "They", "My sister" to "She", "My father" to "He")
        val (noun, pronoun) = nouns[(a + b * 2) % nouns.size]
        val verb = if (pronoun in setOf("We", "They")) "are" else "is"
        val meaning = mapOf("He" to "um homem", "She" to "uma mulher", "It" to "uma coisa ou um animal",
            "We" to "um grupo com você", "They" to "um grupo sem você")
        val others = listOf("He", "She", "It", "We", "They").filter { it != pronoun }
        fill("Troque pelo pronome:", "$noun $verb at home. ___ $verb tired.", pronoun,
            "$noun é ${meaning.getValue(pronoun)}: $pronoun.",
            listOf("Descubra quem é $noun.", "${meaning.getValue(pronoun).cap()} → $pronoun."),
            *others.shuffled(kotlin.random.Random(a * 31 + b)).take(3).map { it to "$it é ${meaning.getValue(it)}." }.toTypedArray())
    },

    english("en-to-be", "Sujeito+am/is/are+complemento", "Sujeito + am/is/are + complemento",
        "Quem, depois o verbo ser ou estar, depois o que ou como.",
        listOf(sym("Sujeito", "quem a frase descreve"), sym("am", "ser/estar só com I"),
            sym("is", "ser/estar com he, she, it ou singular"), sym("are", "ser/estar com you, we, they ou plural"),
            sym("complemento", "o que, como ou onde o sujeito é ou está")),
        listOf(
            step("""I\ am\ tired.""", "I combina só com am."),
            step("""She\ is\ tired.""", "He, she e it combinam com is."),
            step("""They\ are\ tired.""", "You, we e they combinam com are."),
            step("""I'm\ \ she's\ \ they're""", "Na fala, junte as palavras: I'm, she's, they're.")
        ),
        listOf(
            case(Scene.Talk, "Apresentação", "Ao conhecer alguém, você diz nome, origem e profissão, tudo com to be.",
                "I'm Mauro. I'm from Brazil. I'm a developer.",
                fill("Complete a apresentação:", "Hi! My name ___ Mauro.", "is", "My name é uma coisa, singular: is.",
                    listOf("O sujeito é my name (meu nome).", "Singular → is."),
                    "am" to "Am só com I. O sujeito aqui é my name.", "are" to "Are é para plural ou you.")),
            case(Scene.Work, "Entrevista de emprego", "Na entrevista, você descreve suas qualidades com to be.",
                "I am organized. My team is great.",
                fill("Complete:", "I ___ good at programming.", "am", "Com I: am.",
                    listOf("O sujeito é I.", "I → am."),
                    "is" to "Is é para he, she, it.", "are" to "Are é para you, we, they.")),
            case(Scene.Health, "Como você está", "No médico ou numa conversa, você diz como está se sentindo.",
                "I'm sick. My head is hot.",
                fill("Complete:", "My children ___ sick today.", "are", "My children é plural: are.",
                    listOf("My children = they.", "They → are."),
                    "is" to "Is é para um só. Children é plural.", "am" to "Am só com I."))
        ),
        iso("Qual par está certo?", "We are · He is",
            "We usa are. He usa is.",
            "We is · He are" to "As formas estão trocadas.",
            "We am · He am" to "Am só combina com I."),
        listOf(say("I'm tired.", "Estou cansado.", "áim táiard"),
            say("He's at work.", "Ele está no trabalho.", "rríz ét uôrk"),
            say("You're right.", "Você está certo.", "iór ráit"),
            say("We're ready.", "Estamos prontos.", "uír rédi"),
            say("Where are you?", "Onde você está?", "uér ár iú"))
    ) { a, b ->
        val w = who(a, b)
        val comp = listOf("at school", "tired", "from Brazil", "happy", "late", "ready", "at work", "hungry")[(a + b) % 8]
        fill("Complete com am, is ou are:", "${w.subject} ___ $comp.", w.be, beRule(w),
            listOf("O sujeito é ${w.subject}.", beRule(w)),
            *listOf("am", "is", "are").filter { it != w.be }.map {
                it to when (it) { "am" -> "Am só com I."; "is" -> "Is só com he, she, it ou singular."; else -> "Are é para you, we, they ou plural." }
            }.toTypedArray())
    },

    english("en-to-be-questions", "Am/Is/Are+sujeito+?", "Am/Is/Are + sujeito + ?   ·   sujeito + am/is/are + not",
        "Para perguntar, o verbo pula para a frente. Para negar, coloque not depois do verbo.",
        listOf(sym("Am/Is/Are", "o verbo to be no início da pergunta"), sym("sujeito", "quem vem logo depois do verbo"),
            sym("not", "a palavra que nega"), sym("isn't", "forma curta de is not"), sym("aren't", "forma curta de are not")),
        listOf(
            step("""You\ are\ tired.""", "Comece pela frase afirmativa."),
            step("""Are\ you\ tired?""", "Pergunta: troque a ordem, o verbo vai para a frente."),
            step("""You\ are\ not\ tired.""", "Negativa: not logo depois do verbo."),
            step("""Yes,\ I\ am.\ /\ No,\ I'm\ not.""", "Responda curto repetindo o verbo.")
        ),
        listOf(
            case(Scene.Travel, "No hotel", "Você quer saber se o café da manhã está incluído.",
                "Is breakfast included?",
                fill("Monte a pergunta:", "Breakfast is included. → ___", "Is breakfast included?",
                    "O verbo is vai para a frente.",
                    listOf("Ache o verbo: is.", "Coloque is antes do sujeito."),
                    "Breakfast is included?" to "Na escrita a ordem precisa mudar.",
                    "Does breakfast is included?" to "To be não usa does.")),
            case(Scene.Phone, "Mensagem", "Um amigo pergunta se você está em casa. Você responde que não.",
                "Are you at home? No, I'm not.",
                fill("Responda negando:", "Are you at home? No, ___.", "I'm not", "I am not → I'm not.",
                    listOf("A pergunta usa are you. A resposta usa I am.", "Negue: I'm not."),
                    "I amn't" to "Amn't não existe.", "I don't" to "Don't é para verbos de ação."))
        ),
        iso("Como negar \"He is busy\"?", "He isn't busy.",
            "Coloque not depois de is: is not → isn't.",
            "He doesn't busy." to "Com to be não se usa doesn't.",
            "He not is busy." to "Not vem depois do verbo, não antes."),
        listOf(say("Are you OK?", "Você está bem?", "ár iú ou-kêi"),
            say("Is it far?", "É longe?", "iz it fár"),
            say("I'm not sure.", "Não tenho certeza.", "áim nót xúr"),
            say("It isn't expensive.", "Não é caro.", "it ízant ekspénsiv"),
            say("Yes, I am.", "Sim, estou/sou.", "iés ai ém"))
    ) { a, b ->
        val w = who(a, b)
        val comp = listOf("at home", "tired", "ready", "busy", "late", "happy", "hungry", "at work")[(a * b) % 8]
        val subject = w.inside()
        if (b % 2 == 0) {
            val answer = "${w.be.cap()} $subject $comp?"
            fill("Transforme em pergunta:", "${w.subject} ${w.be} $comp. → ___", answer, "O verbo ${w.be} vai para antes do sujeito.",
                listOf("Ache o verbo: ${w.be}.", "Coloque ${w.be} na frente: $answer"),
                "${w.subject} ${w.be} $comp?" to "Só o ponto de interrogação não basta. Inverta a ordem.",
                "${if (w.third) "Does" else "Do"} $subject ${w.be} $comp?" to "To be não usa do/does.")
        } else {
            val negative = when (w.be) { "am" -> "I'm not"; "is" -> "${w.subject} isn't"; else -> "${w.subject} aren't" }
            fill("Negue a frase:", "${w.subject} ${w.be} $comp. → ___ $comp.", negative, "Not vem depois do verbo: ${w.be} not.",
                listOf("Ache o verbo: ${w.be}.", "Coloque not depois dele: $negative."),
                "${w.subject} ${if (w.third) "doesn't" else "don't"}" to "Don't e doesn't são para verbos de ação, não para to be.",
                "${w.subject} not ${w.be}" to "Not vem depois do verbo.")
        }
    },

    english("en-possessives", "Possessivo+coisa", "my · your · his · her · its · our · their + coisa",
        "O possessivo vem antes da coisa e depende de quem é o dono.",
        listOf(sym("my", "meu ou minha"), sym("your", "seu, sua, de vocês"), sym("his", "dele, dono homem"),
            sym("her", "dela, dona mulher"), sym("our", "nosso ou nossa"), sym("their", "deles ou delas")),
        listOf(
            step("""Pedro\ +\ car""", "Pergunte: de quem é? Do Pedro."),
            step("""his\ car""", "Dono homem → his. Não importa se a coisa é masculina ou feminina."),
            step("""Ana\ +\ car\ →\ her\ car""", "Dona mulher → her."),
            step("""Pedro's\ car""", "Com nome, use 's: o carro do Pedro.")
        ),
        listOf(
            case(Scene.Phone, "Trocar contato", "Para passar o seu número, você usa my.",
                "My number is 99 1234 5678.",
                fill("Complete:", "What's ___ phone number? (seu)", "your", "Seu, de você: your.",
                    listOf("O dono é você.", "You → your."),
                    "you" to "You é sujeito. Antes de uma coisa use your.", "his" to "His é dele.")),
            case(Scene.Home, "Família", "Falando da família de um amigo, o possessivo segue o amigo.",
                "This is Ana. Her brother is a doctor.",
                fill("Complete (a mãe do Lucas):", "Lucas called ___ mother.", "his", "O dono é o Lucas, homem: his.",
                    listOf("A mãe é de quem? Do Lucas.", "Dono homem → his."),
                    "her" to "Her seria se a dona fosse mulher. A mãe ser mulher não importa.", "he" to "He é sujeito."))
        ),
        iso("\"A casa dele\" em inglês é:", "his house",
            "O dono é ele: his. A casa ser feminina em português não muda nada.",
            "her house" to "Her é dela. O dono é ele.",
            "he house" to "He é sujeito. Antes de uma coisa use his."),
        listOf(say("This is my brother.", "Este é o meu irmão.", "dís iz mai brâdar"),
            say("What's your name?", "Qual é o seu nome?", "uóts iór nêim"),
            say("Her name is Ana.", "O nome dela é Ana.", "rrâr nêim iz ána"),
            say("Their house is big.", "A casa deles é grande.", "dér ráus iz big"),
            say("Our team won!", "Nosso time ganhou!", "áuar tím uón"))
    ) { a, b ->
        data class Owner(val subject: String, val verb: String, val pronoun: String, val poss: String, val contrast: String)
        val owners = listOf(Owner("I", "love", "I", "my", "your"), Owner("You", "love", "you", "your", "my"),
            Owner("He", "loves", "he", "his", "her"), Owner("She", "loves", "she", "her", "his"),
            Owner("We", "love", "we", "our", "their"), Owner("They", "love", "they", "their", "our"),
            Owner("Pedro", "loves", "he", "his", "her"), Owner("Ana", "loves", "she", "her", "his"),
            Owner("My parents", "love", "they", "their", "our"))
        val o = owners[(a + b * 3) % owners.size]
        val thing = listOf("car", "phone", "house", "dog", "job", "family", "city", "team")[(a * b) % 8]
        fill("Complete com o possessivo:", "${o.subject} ${o.verb} ___ $thing.", o.poss,
            "O dono é ${o.subject} (${o.pronoun}): ${o.poss}.",
            listOf("Pergunte: de quem é? De ${o.subject}.", "${o.pronoun.cap()} → ${o.poss}."),
            o.contrast to "${o.contrast.cap()} indica outro dono.",
            (if (o.pronoun == "I") "me" else o.pronoun) to "${if (o.pronoun == "I") "Me" else o.pronoun.cap()} não é possessivo. Antes de uma coisa use ${o.poss}.")
    },

    english("en-articles", "a/an+coisa\\ qualquer\\quad the+coisa\\ específica", "a / an + algo qualquer · the + algo específico",
        "Um qualquer: a ou an, pelo som. Algo específico: the.",
        listOf(sym("a", "um ou uma, antes de som de consoante"), sym("an", "um ou uma, antes de som de vogal"),
            sym("the", "o, a, os, as, algo específico")),
        listOf(
            step("""a\ car\quad a\ house""", "Som de consoante no início: a."),
            step("""an\ apple\quad an\ hour""", "Som de vogal: an. Em hour o h é mudo."),
            step("""a\ university""", "University começa com som de \"iu\", que é de consoante: a."),
            step("""I\ have\ a\ car.\ The\ car\ is\ red.""", "Primeira vez: a. Depois que já se sabe qual: the.")
        ),
        listOf(
            case(Scene.Work, "Profissão", "Para dizer a profissão, use a ou an.",
                "I'm a developer. She's an architect.",
                fill("Complete:", "She is ___ architect.", "an", "Architect começa com som de vogal: an.",
                    listOf("Ouça o começo: \"árquitekt\", som de vogal.", "Som de vogal → an."),
                    "a" to "A é para som de consoante.", "the" to "Profissão leva a/an.")),
            case(Scene.Travel, "Na cafeteria", "Pedindo qualquer café: a. Depois, falando do café que veio: the.",
                "A coffee, please. The coffee is cold.",
                fill("Complete:", "I ordered a juice. ___ juice is very sweet.", "The", "O suco já foi mencionado: the.",
                    listOf("O suco já apareceu na frase anterior.", "Específico → the."),
                    "A" to "A apresenta algo novo.", "An" to "An também apresenta algo novo."))
        ),
        iso("Qual está certo?", "an hour · a university",
            "Vale o som: hour começa com som de vogal e university com som de \"iu\".",
            "a hour · an university" to "Você olhou a letra, não o som.",
            "an hour · an university" to "University soa \"iu\", som de consoante."),
        listOf(say("I'm a student.", "Sou estudante.", "áim a stiúdent"),
            say("An apple, please.", "Uma maçã, por favor.", "an épol plíz"),
            say("The bus is late.", "O ônibus está atrasado.", "dâ bâs iz lêit"),
            say("It takes an hour.", "Leva uma hora.", "it têiks an áuar"))
    ) { a, b ->
        val words = listOf("apple" to "an", "banana" to "a", "hour" to "an", "university" to "a", "egg" to "an",
            "car" to "a", "umbrella" to "an", "house" to "a", "idea" to "an", "uniform" to "a")
        val (word, article) = words[(a * 2 + b) % words.size]
        val tricky = word in setOf("hour", "university", "uniform")
        val sound = if (article == "an") "som de vogal" else "som de consoante"
        fill("Complete com a ou an:", "I need ___ $word.", article, "$word começa com $sound: $article.",
            listOf(if (tricky) "Cuidado: a primeira letra engana. Ouça o som de $word." else "Ouça o início de $word.",
                "${sound.cap()} → $article."),
            (if (article == "a") "an" else "a") to "Vale o som do início da palavra, não a letra. $word tem $sound.")
    },

    english("en-there-is", "There+is/are+coisa+lugar", "There is / There are + coisa + lugar",
        "Há uma coisa: there is. Há várias: there are.",
        listOf(sym("There is", "há, existe, para uma coisa"), sym("There are", "há, existem, para várias coisas"),
            sym("coisa", "o que existe"), sym("lugar", "onde existe")),
        listOf(
            step("""There\ is\ a\ bank\ near\ here.""", "Uma coisa: there is."),
            step("""There\ are\ two\ banks.""", "Várias: there are."),
            step("""Is\ there\ a\ bank?""", "Pergunta: inverta para Is there / Are there."),
            step("""There\ isn't\ a\ bank.""", "Negativa: isn't ou aren't.")
        ),
        listOf(
            case(Scene.Travel, "Pedindo informação", "Na rua, você quer saber se há uma farmácia perto.",
                "Excuse me, is there a pharmacy near here?",
                fill("Complete a pergunta:", "Excuse me, ___ a pharmacy near here?", "is there", "Pergunta com uma coisa: is there.",
                    listOf("Uma farmácia: singular.", "Pergunta: inverta para is there."),
                    "there is" to "Na pergunta, is vem antes de there.", "have" to "Have é possuir, não existir.")),
            case(Scene.Home, "Descrevendo a casa", "Para dizer quantos quartos tem o apartamento.",
                "There are two bedrooms and there is one bathroom.",
                fill("Complete:", "___ three bedrooms in my house.", "There are", "Três quartos: plural.",
                    listOf("Conte: três quartos.", "Plural → there are."),
                    "There is" to "There is é para uma coisa só.", "Have" to "Have não indica existência."))
        ),
        iso("\"Tem um mercado aqui perto\" em inglês é:", "There is a market near here.",
            "Tem, no sentido de existir, é there is.",
            "Have a market near here." to "Have é possuir e a frase ficou sem sujeito.",
            "It has a market near here." to "Para dizer que algo existe num lugar, use there is."),
        listOf(say("Is there a bathroom here?", "Tem banheiro aqui?", "iz dér a bétrum rríar"),
            say("There is a problem.", "Tem um problema.", "dér iz a próblem"),
            say("There are many people.", "Tem muita gente.", "dér ár méni pípol"),
            say("There isn't any milk.", "Não tem leite.", "dér ízant éni milk"))
    ) { a, b ->
        val things = listOf("a bank" to false, "two cafés" to true, "many people" to true, "a problem" to false,
            "three bedrooms" to true, "an elevator" to false, "some books" to true, "a cat" to false)
        val (thing, plural) = things[(a + b) % things.size]
        val place = listOf("near here", "in the kitchen", "on the table", "in my city", "in the room", "at school")[(a * b) % 6]
        val answer = if (plural) "There are" else "There is"
        fill("Complete:", "___ $thing $place.", answer, if (plural) "$thing é plural: there are." else "$thing é uma coisa só: there is.",
            listOf("Veja se é uma ou várias coisas: $thing.", if (plural) "Várias → there are." else "Uma → there is."),
            (if (plural) "There is" else "There are") to "Confira o número: $thing é ${if (plural) "plural" else "singular"}.",
            "Have" to "Have é possuir. Para existir, use there is/are.")
    },

    english("en-present", "Sujeito+verbo(+s\\ com\\ he/she/it)", "Sujeito + verbo (+ s com he, she, it)",
        "Rotina e hábitos: o verbo fica igual, mas ganha -s com he, she e it.",
        listOf(sym("Sujeito", "quem faz a ação"), sym("verbo", "a ação, na forma básica"),
            sym("+s", "a terminação para he, she, it"), sym("+es", "a terminação depois de o, s, sh, ch, x"),
            sym("+ies", "a troca de consoante + y")),
        listOf(
            step("""I\ work.\quad They\ work.""", "I, you, we, they: verbo puro."),
            step("""She\ works.""", "He, she, it: verbo + s."),
            step("""He\ watches.\quad She\ goes.""", "Depois de ch, sh, s, x ou o: + es."),
            step("""study\ →\ studies.\quad have\ →\ has""", "Consoante + y vira ies. Have vira has.")
        ),
        listOf(
            case(Scene.Work, "Falando do trabalho", "Para dizer o que você e outras pessoas fazem no dia a dia.",
                "I work with software. My sister works at a hospital.",
                fill("Complete:", "My sister ___ at a hospital.", "works", "My sister = she: works.",
                    listOf("My sister = she.", "She → verbo + s."),
                    "work" to "Com she, o verbo ganha -s.", "is work" to "Não junte is com outro verbo no presente simples.")),
            case(Scene.Games, "Rotina e hobbies", "Contando o que você faz no tempo livre.",
                "I play games at night. My brother watches series.",
                fill("Complete:", "My brother ___ series every night.", "watches", "He + watch: termina em ch, ganha -es.",
                    listOf("My brother = he.", "Watch termina em ch → watches."),
                    "watchs" to "Depois de ch, a terminação é -es.", "watch" to "Com he, o verbo precisa da terminação."))
        ),
        iso("Qual frase está certa?", "She has a car.",
            "Have é irregular: com he, she, it vira has.",
            "She haves a car." to "Have não ganha -s: vira has.",
            "She have a car." to "Com she o verbo muda: has."),
        listOf(say("I work from home.", "Eu trabalho de casa.", "ai uôrk fróm rrôum"),
            say("She lives in São Paulo.", "Ela mora em São Paulo.", "xí livz in são páulo"),
            say("He plays the guitar.", "Ele toca violão.", "rri plêiz dâ guitár"),
            say("My dad watches the news.", "Meu pai assiste ao jornal.", "mai déd uótxiz dâ niúz"),
            say("I always wake up early.", "Eu sempre acordo cedo.", "ai ólueiz uêik âp ârli"))
    ) { a, b ->
        val w = who(a, b)
        data class V(val base: String, val third: String, val comp: String)
        val verbs = listOf(V("work", "works", "in an office"), V("live", "lives", "in Brazil"), V("play", "plays", "soccer"),
            V("watch", "watches", "TV at night"), V("study", "studies", "English"), V("go", "goes", "to school by bus"),
            V("have", "has", "a dog"), V("like", "likes", "pizza"), V("drink", "drinks", "coffee"), V("read", "reads", "books"))
        val v = verbs[(a * 2 + b * 3) % verbs.size]
        val answer = if (w.third) v.third else v.base
        fill("Complete com o verbo ${v.base}:", "${w.subject} ___ ${v.comp}.", answer,
            if (w.third) "${w.subject} equivale a ${w.pronoun}: o verbo vira ${v.third}." else "${w.subject} não é he, she nem it: verbo puro.",
            listOf("O sujeito é ${w.named()}.", if (w.third) "He, she, it → ${v.third}." else "Sem -s → ${v.base}."),
            (if (w.third) v.base else v.third) to (if (w.third) "Com ${w.pronoun}, o verbo muda para ${v.third}." else "O -s é só para he, she, it."),
            "${w.be} ${v.base}" to "Não junte am/is/are com outro verbo no presente simples.")
    },

    english("en-do-does", "Do/Does+sujeito+verbo+?", "Do/Does + sujeito + verbo? · sujeito + don't/doesn't + verbo",
        "Verbos de ação precisam de ajudante: do, ou does com he, she, it. O verbo volta ao normal.",
        listOf(sym("Do", "auxiliar de pergunta para I, you, we, they"), sym("Does", "auxiliar de pergunta para he, she, it"),
            sym("don't", "do not, negativa com I, you, we, they"), sym("doesn't", "does not, negativa com he, she, it"),
            sym("verbo", "a ação, sempre na forma básica depois do auxiliar")),
        listOf(
            step("""She\ works.""", "Frase afirmativa: o -s está no verbo."),
            step("""Does\ she\ work?""", "Pergunta: does entra na frente e leva o -s embora."),
            step("""She\ doesn't\ work.""", "Negativa: doesn't + verbo puro."),
            step("""Do\ you\ work?\ Yes,\ I\ do.""", "Resposta curta: repita o auxiliar.")
        ),
        listOf(
            case(Scene.Talk, "Conhecendo alguém", "As primeiras perguntas de uma conversa usam do.",
                "Where do you live? Do you like music?",
                fill("Complete:", "___ you like music?", "Do", "You → do.",
                    listOf("Like é verbo de ação: precisa de auxiliar.", "You → do."),
                    "Does" to "Does é para he, she, it.", "Are" to "Like é verbo de ação, não usa to be.")),
            case(Scene.Work, "Falando de um colega", "Para dizer o que alguém não faz.",
                "He doesn't drink coffee.",
                fill("Negue:", "He drinks coffee. → He ___ coffee.", "doesn't drink", "Doesn't já tem o -s. O verbo fica puro.",
                    listOf("He → doesn't.", "Depois de doesn't: drink, sem s."),
                    "doesn't drinks" to "O -s já foi para o doesn't.", "don't drink" to "Com he, use doesn't."))
        ),
        iso("Qual pergunta está certa?", "Does she speak English?",
            "Does + she + verbo puro.",
            "Does she speaks English?" to "Depois de does, o verbo fica sem -s.",
            "Is she speak English?" to "Speak é verbo de ação: use does."),
        listOf(say("Do you speak English?", "Você fala inglês?", "du iú spík ínglix"),
            say("I don't understand.", "Eu não entendo.", "ai dônt ândarstend"),
            say("Does it work?", "Funciona?", "dâz it uôrk"),
            say("She doesn't know.", "Ela não sabe.", "xí dâzant nôu"),
            say("Where do you live?", "Onde você mora?", "uér du iú liv"))
    ) { a, b ->
        val w = who(a, b)
        val verbs = listOf("like pizza", "play soccer", "speak English", "work on Saturday", "live here", "drink coffee", "watch anime", "drive")
        val verb = verbs[(a + b * 3) % verbs.size]
        val subject = w.inside()
        if (b % 2 == 0) {
            val answer = if (w.third) "Does" else "Do"
            fill("Complete a pergunta:", "___ $subject $verb?", answer,
                if (w.third) "${w.named()}: does." else "${w.subject} não é he, she nem it: do.",
                listOf("O verbo é de ação: precisa de do ou does.", "${w.pronoun.cap()} → $answer."),
                (if (w.third) "Do" else "Does") to "Confira o sujeito: ${w.subject} pede $answer.",
                w.be.cap() to "${verb.substringBefore(' ').cap()} é verbo de ação. To be não serve de auxiliar aqui.")
        } else {
            val answer = if (w.third) "doesn't" else "don't"
            fill("Complete a negativa:", "${w.subject} ___ ${verb}.", answer,
                if (w.third) "${w.pronoun.cap()} → doesn't." else "${w.pronoun.cap()} → don't.",
                listOf("Negativa de verbo de ação: don't ou doesn't.", "${w.pronoun.cap()} → $answer."),
                (if (w.third) "don't" else "doesn't") to "Confira o sujeito: ${w.subject} pede $answer.",
                (if (w.be == "am") "am not" else "${w.be}n't") to "Com verbo de ação, a negativa usa don't/doesn't.")
        }
    },

    english("en-continuous", "Sujeito+am/is/are+(verbo+ing)", "Sujeito + am/is/are + verbo-ing",
        "Agora, neste momento: to be mais o verbo com -ing.",
        listOf(sym("am/is/are", "o to be, que muda com o sujeito"), sym("verbo+ing", "a ação em andamento, fazendo"),
            sym("now", "agora, marcador de tempo")),
        listOf(
            step("""I\ am\ +\ study""", "Escolha o to be certo para o sujeito."),
            step("""study\ →\ studying""", "Coloque -ing no verbo."),
            step("""I\ am\ studying\ now.""", "Pronto: estou estudando agora."),
            step("""make\ →\ making\quad run\ →\ running""", "E final cai. Vogal curta + consoante dobra.")
        ),
        listOf(
            case(Scene.Phone, "What are you doing?", "Quando alguém pergunta o que você está fazendo por mensagem.",
                "I'm watching a movie.",
                fill("Responda:", "What are you doing? I ___ dinner.", "am cooking", "Agora: am + cooking.",
                    listOf("A pergunta é sobre agora.", "I → am + cooking."),
                    "cook" to "Cook sozinho é rotina, não agora.", "cooking" to "Falta o am.")),
            case(Scene.Games, "Partida online", "No jogo, o time avisa o que está acontecendo.",
                "They're coming! We're losing!",
                fill("Complete:", "Hurry! The enemies ___ now.", "are coming", "The enemies = they: are coming.",
                    listOf("The enemies = they.", "They → are + coming."),
                    "is coming" to "Enemies é plural: are.", "come" to "Now indica agora: use to be + -ing."))
        ),
        iso("Qual frase fala de agora?", "She is sleeping.",
            "To be + -ing: está acontecendo agora.",
            "She sleeps." to "Presente simples: hábito.",
            "She sleeping." to "Falta o is."),
        listOf(say("I'm working now.", "Estou trabalhando agora.", "áim uôrkin náu"),
            say("What are you doing?", "O que você está fazendo?", "uót ár iú dúin"),
            say("It's raining.", "Está chovendo.", "its rêinin"),
            say("We're waiting for you.", "Estamos te esperando.", "uír uêitin for iú"))
    ) { a, b ->
        val w = who(a, b)
        val verbs = listOf("work" to "working", "read" to "reading", "cook" to "cooking", "run" to "running", "write" to "writing",
            "swim" to "swimming", "study" to "studying", "play" to "playing", "dance" to "dancing", "sleep" to "sleeping")
        val (base, ing) = verbs[(a * 3 + b) % verbs.size]
        fill("Complete com o verbo $base (agora):", "${w.subject} ___ right now.", "${w.be} $ing",
            "${beRule(w)} E o verbo com -ing: $ing.",
            listOf(beRule(w), "Verbo com -ing: $ing."),
            ing to "Falta o to be (${w.be}).",
            "${w.be} $base" to "Depois do to be, o verbo precisa de -ing.",
            (if (w.third) third(base) else base) to "Esse é o presente simples, para rotina.")
    },

    english("en-object-pronouns", "Sujeito+verbo+objeto", "Sujeito + verbo + me / you / him / her / it / us / them",
        "Quem recebe a ação vem depois do verbo, na forma de objeto.",
        listOf(sym("me", "mim, me"), sym("him", "ele, o, lhe"), sym("her", "ela, a, lhe"), sym("us", "nós, nos"),
            sym("them", "eles, elas, os, as")),
        listOf(
            step("""He\ loves\ +\ (eu)""", "Ele me ama: quem recebe a ação sou eu."),
            step("""He\ loves\ me.""", "O objeto vai para depois do verbo e vira me."),
            step("""I\ →\ me,\ she\ →\ her,\ they\ →\ them""", "Cada sujeito tem sua forma de objeto."),
            step("""with\ me,\ for\ them""", "Depois de preposição também se usa objeto.")
        ),
        listOf(
            case(Scene.Phone, "Ligação", "Pedindo para alguém te ligar depois.",
                "Call me later!",
                fill("Complete:", "Please, call ___ later. (eu)", "me", "Eu como objeto: me.",
                    listOf("Quem recebe a ligação? Eu.", "Eu → me."),
                    "I" to "I é sujeito. Depois do verbo use me.", "my" to "My é possessivo.")),
            case(Scene.Talk, "Saindo com amigos", "Convidando alguém a ir junto.",
                "Come with us!",
                fill("Complete:", "Do you want to come with ___? (nós)", "us", "Nós como objeto: us.",
                    listOf("Depois de with vem objeto.", "Nós → us."),
                    "we" to "We é sujeito.", "our" to "Our é possessivo."))
        ),
        iso("\"Eu a conheço\" em inglês:", "I know her.",
            "Ela como objeto: her, depois do verbo.",
            "I her know." to "O objeto vem depois do verbo.",
            "I know she." to "She é sujeito. Depois do verbo use her."),
        listOf(say("Help me, please.", "Me ajuda, por favor.", "rrélp mí plíz"),
            say("I love you.", "Eu te amo.", "ai lâv iú"),
            say("Tell him I said hi.", "Diga a ele que mandei um oi.", "tél rrim ai séd rrái"),
            say("Wait for us!", "Espere por nós!", "uêit for âs"))
    ) { a, b ->
        data class O(val pt: String, val subject: String, val obj: String, val possessive: String)
        val objects = listOf(O("eu", "I", "me", "my"), O("ele", "he", "him", "his"), O("ela", "she", "her", "his"),
            O("nós", "we", "us", "our"), O("eles", "they", "them", "their"))
        val o = objects[(a + b) % objects.size]
        val phrase = listOf("Can you help ___?", "Please call ___ later.", "I'm waiting for ___.", "She talks to ___ every day.",
            "We visit ___ on Sundays.", "Listen to ___!", "This gift is for ___.")[(a * b) % 7]
        fill("Complete (${o.pt}):", phrase, o.obj, "${o.pt.cap()} recebendo a ação: ${o.obj}.",
            listOf("O espaço vem depois do verbo ou da preposição: é objeto.", "${o.pt.cap()} → ${o.obj}."),
            o.subject to "${o.subject.cap()} é sujeito, fica antes do verbo.",
            o.possessive to "${o.possessive.cap()} é possessivo, vai antes de uma coisa.")
    },

    english("en-can", "Sujeito+can/can't+verbo", "Sujeito + can / can't + verbo",
        "Can é poder ou conseguir. Igual para todos e com o verbo puro depois.",
        listOf(sym("can", "posso, consigo, sei fazer"), sym("can't", "não posso, não consigo"),
            sym("verbo", "a ação na forma básica, sem to e sem -s")),
        listOf(
            step("""I\ can\ swim.""", "Can + verbo puro."),
            step("""She\ can\ swim.""", "Com she nada muda: nem can nem o verbo ganham -s."),
            step("""Can\ you\ swim?""", "Pergunta: can vai para a frente."),
            step("""Could\ you\ help\ me?""", "Pedido mais educado: could.")
        ),
        listOf(
            case(Scene.Travel, "No restaurante", "Pedindo a conta.",
                "Can I have the bill, please?",
                fill("Complete:", "___ I have the bill, please?", "Can", "Pedido: can antes do sujeito.",
                    listOf("É uma pergunta-pedido.", "Can vai para a frente: Can I…?"),
                    "Do" to "Can não usa do.", "Am" to "Am não expressa poder.")),
            case(Scene.Work, "Habilidades", "Numa entrevista, dizendo o que você sabe fazer.",
                "I can speak Portuguese and Spanish.",
                fill("Complete:", "She ___ three languages.", "can speak", "Can + verbo puro.",
                    listOf("Can é igual para todos.", "Depois dele: speak, sem -s."),
                    "can speaks" to "Depois de can o verbo fica sem -s.", "cans speak" to "Can nunca ganha -s."))
        ),
        iso("Qual está certa?", "He can drive.",
            "Can + verbo puro, igual para todos.",
            "He can to drive." to "Depois de can não se usa to.",
            "He can drives." to "O verbo fica sem -s depois de can."),
        listOf(say("Can you help me?", "Você pode me ajudar?", "kén iú rrélp mí"),
            say("I can't hear you.", "Não consigo te ouvir.", "aiként rríar iú"),
            say("Can I come in?", "Posso entrar?", "kén ai câm in"),
            say("Could you repeat, please?", "Pode repetir, por favor?", "kúd iú ripít plíz"))
    ) { a, b ->
        val w = who(a, b)
        val verbs = listOf("swim", "drive", "cook", "dance", "speak English", "play the guitar", "run fast", "fix computers")
        val verb = verbs[(a * b + a) % verbs.size]
        val base = verb.substringBefore(' ')
        val rest = verb.removePrefix(base)
        val negative = b % 3 == 0
        val answer = (if (negative) "can't " else "can ") + base
        fill("Complete (${if (negative) "não consegue" else "consegue"}):", "${w.subject} ___$rest.", answer,
            "Can${if (negative) "'t" else ""} + verbo puro, igual para todos.",
            listOf("Can não muda com o sujeito.", "Depois dele, o verbo puro: $base."),
            (if (negative) "can't ${base}s" else "can ${base}s") to "Depois de can o verbo fica sem -s.",
            (if (negative) "can't to $base" else "can to $base") to "Depois de can não se usa to.",
            (if (negative) "don't can $base" else "cans $base") to if (negative) "A negativa é can't, sem do." else "Can nunca ganha -s.")
    },

    english("en-wh", "Wh+do/does\\ ou\\ to\\ be+sujeito+?", "What / Where / When / Who / Why / How + pergunta",
        "A palavra de pergunta vem primeiro. Depois, a pergunta normal com do, does ou to be.",
        listOf(sym("What", "o que, qual"), sym("Where", "onde"), sym("When", "quando"), sym("Who", "quem"),
            sym("Why", "por quê"), sym("How", "como"), sym("How much", "quanto custa, quanto")),
        listOf(
            step("""Do\ you\ live\ here?""", "Comece com a pergunta de sim ou não."),
            step("""Where\ do\ you\ live?""", "Coloque a palavra de pergunta na frente."),
            step("""Is\ the\ party\ today?\ →\ When\ is\ the\ party?""", "Com to be funciona igual."),
            step("""Why?\ Because…""", "Why pergunta, because responde.")
        ),
        listOf(
            case(Scene.Travel, "Pedindo direções", "Perdido na cidade, você pergunta onde fica a estação.",
                "Where is the train station?",
                fill("Complete:", "Excuse me, ___ is the station?", "where", "Lugar: where.",
                    listOf("Você quer saber um lugar.", "Lugar → where."),
                    "when" to "When pergunta tempo.", "who" to "Who pergunta pessoa.")),
            case(Scene.Money, "Na loja", "Perguntando o preço.",
                "How much is this shirt?",
                fill("Complete:", "___ is this shirt?", "How much", "Preço: how much.",
                    listOf("Você quer saber o preço.", "Preço → how much."),
                    "How many" to "How many é para contar coisas.", "What" to "What sozinho pergunta \"o que\".")),
            case(Scene.Talk, "Primeira conversa", "Perguntas clássicas ao conhecer alguém.",
                "What do you do? How old are you?",
                fill("Complete:", "___ old are you?", "How", "Idade: how old.",
                    listOf("Idade em inglês é how old.", "Complete com how."),
                    "What" to "Em inglês não se diz what old.", "Who" to "Who pergunta pessoa."))
        ),
        iso("Monte \"O que você faz?\" (trabalho)", "What do you do?",
            "What + do (auxiliar) + you + do (verbo fazer).",
            "What you do?" to "Falta o auxiliar do.",
            "What are you do?" to "Do é verbo de ação: use do como auxiliar, não are."),
        listOf(say("Where are you from?", "De onde você é?", "uér ár iú fróm"),
            say("What time is it?", "Que horas são?", "uót táim iz it"),
            say("How much is it?", "Quanto custa?", "rráu mâtx iz it"),
            say("Why not?", "Por que não?", "uái nót"),
            say("Who is she?", "Quem é ela?", "rrú iz xí"))
    ) { a, b ->
        val questions = listOf(Triple("um lugar", "Where", "___ is the bathroom?"), Triple("a hora", "What time", "___ does the store open?"),
            Triple("uma pessoa", "Who", "___ is your teacher?"), Triple("o motivo", "Why", "___ are you sad?"),
            Triple("o modo", "How", "___ do you go to work?"), Triple("o preço", "How much", "___ is this shirt?"),
            Triple("a quantidade", "How many", "___ brothers do you have?"), Triple("a idade", "How old", "___ are you?"),
            Triple("a data", "When", "___ is your birthday?"), Triple("uma coisa", "What", "___ do you want?"))
        val q = questions[(a * 3 + b) % questions.size]
        val others = questions.filter { it.second != q.second }.shuffled(kotlin.random.Random(a * 17 + b)).take(2)
        fill("Você quer saber ${q.first}:", q.third, q.second, "${q.first.cap()} → ${q.second}.",
            listOf("Pense no que você quer saber: ${q.first}.", "${q.first.cap()} → ${q.second}."),
            *others.map { it.second to "${it.second} pergunta ${it.first}." }.toTypedArray())
    },

    english("en-time", "in\\ (mês,\\ ano)\\quad on\\ (dia)\\quad at\\ (hora)", "in (mês, ano) · on (dia) · at (hora)",
        "Pirâmide do tempo: in para o grande, on para o dia, at para a hora.",
        listOf(sym("in", "meses, anos, estações e partes do dia"), sym("on", "dias da semana e datas"),
            sym("at", "horas exatas e momentos")),
        listOf(
            step("""in\ 2025,\ in\ May""", "Períodos grandes: in."),
            step("""on\ Friday,\ on\ May\ 5th""", "Um dia: on."),
            step("""at\ 7\ pm,\ at\ noon""", "Hora: at."),
            step("""in\ the\ morning,\ at\ night""", "Exceção para gravar: at night.")
        ),
        listOf(
            case(Scene.Work, "Marcando reunião", "Combinando uma reunião por e-mail.",
                "The meeting is on Monday at 10 am.",
                fill("Complete:", "The meeting is on Monday ___ 10 am.", "at", "Hora exata: at.",
                    listOf("10 am é uma hora.", "Hora → at."),
                    "in" to "In é para mês, ano ou parte do dia.", "on" to "On é para dia.")),
            case(Scene.Health, "Consulta médica", "Confirmando a data da consulta.",
                "My appointment is on June 3rd.",
                fill("Complete:", "My appointment is ___ June 3rd.", "on", "Data específica: on.",
                    listOf("June 3rd é um dia.", "Dia → on."),
                    "in" to "In June serviria para o mês inteiro. Com o dia, use on.", "at" to "At é para hora."))
        ),
        iso("Qual está certa?", "in July · on Sunday · at 5 pm",
            "Mês: in. Dia: on. Hora: at.",
            "on July · in Sunday · at 5 pm" to "Mês usa in e dia usa on.",
            "in July · on Sunday · in 5 pm" to "Hora usa at."),
        listOf(say("See you on Friday.", "Até sexta.", "sí iú ón fráidei"),
            say("I wake up at 6.", "Acordo às 6.", "ai uêik âp ét síks"),
            say("I was born in 2004.", "Nasci em 2004.", "ai uóz bórn in tú táuzand fór"),
            say("I study at night.", "Estudo à noite.", "ai stâdi ét náit"))
    ) { a, b ->
        val expressions = listOf("Monday" to "on", "May" to "in", "7 pm" to "at", "2026" to "in", "Christmas Day" to "on",
            "night" to "at", "the morning" to "in", "noon" to "at", "my birthday" to "on", "summer" to "in")
        val (expression, preposition) = expressions[(a + b * 5) % expressions.size]
        val kind = mapOf("in" to "período grande", "on" to "dia", "at" to "hora ou momento")
        fill("Complete com in, on ou at:", "See you ___ $expression.", preposition,
            "$expression é ${kind.getValue(preposition)}: $preposition.",
            listOf("Classifique: $expression é ${kind.getValue(preposition)}.", "${kind.getValue(preposition).cap()} → $preposition."),
            *listOf("in", "on", "at").filter { it != preposition }.map { it to "$it é para ${kind.getValue(it)}." }.toTypedArray())
    },

    english("en-was-were", "Sujeito+was/were+complemento", "Sujeito + was / were + complemento",
        "O to be no passado: was para I, he, she, it e were para you, we, they.",
        listOf(sym("was", "era, estava, com I, he, she, it"), sym("were", "eram, estavam, com you, we, they"),
            sym("wasn't", "was not"), sym("weren't", "were not")),
        listOf(
            step("""I\ am\ tired\ →\ I\ was\ tired""", "Am e is viram was."),
            step("""They\ are\ here\ →\ They\ were\ here""", "Are vira were."),
            step("""Were\ you\ at\ home?""", "Pergunta: inverta, igual ao presente."),
            step("""I\ was\ born\ in\ 2004.""", "Nascer usa was born.")
        ),
        listOf(
            case(Scene.Talk, "Como foi o fim de semana", "Contando como foi a festa.",
                "It was amazing! The people were nice.",
                fill("Complete:", "The party ___ amazing!", "was", "The party = it: was.",
                    listOf("The party = it.", "It → was."),
                    "were" to "Were é para you, we, they.", "is" to "A festa já aconteceu: passado.")),
            case(Scene.Work, "Explicando um atraso", "Dizendo onde você estava.",
                "Sorry, I was in a meeting.",
                fill("Complete:", "Sorry, we ___ in traffic.", "were", "We: were.",
                    listOf("O sujeito é we.", "We → were."),
                    "was" to "Was é para I, he, she, it.", "are" to "Você está explicando algo que já passou."))
        ),
        iso("Qual está certa?", "You were late yesterday.",
            "You usa were, mesmo falando de uma pessoa só.",
            "You was late yesterday." to "You sempre usa were.",
            "You are late yesterday." to "Yesterday pede passado."),
        listOf(say("It was great!", "Foi ótimo!", "it uóz grêit"),
            say("Where were you?", "Onde você estava?", "uér uâr iú"),
            say("I wasn't there.", "Eu não estava lá.", "ai uózant dér"),
            say("We were friends.", "Nós éramos amigos.", "uí uâr fréndz"))
    ) { a, b ->
        val w = who(a, b)
        val comp = listOf("at the beach", "very tired", "at home", "sick", "happy", "late", "at the party", "busy")[(a + b * 2) % 8]
        fill("Complete com was ou were:", "${w.subject} ___ $comp yesterday.", w.past,
            "${w.named()} no passado: ${w.past}.",
            listOf("Yesterday: passado.", "${w.pronoun.cap()} → ${w.past}."),
            (if (w.past == "was") "were" else "was") to "Confira o sujeito: ${w.subject} pede ${w.past}.",
            w.be to "Yesterday pede o passado, não ${w.be}.")
    },

    english("en-past", "Sujeito+(verbo+ed)\\ ou\\ irregular", "Sujeito + verbo-ed (ou irregular) · Did + sujeito + verbo?",
        "Passado: -ed nos regulares, forma própria nos irregulares. Pergunta e negativa com did e verbo puro.",
        listOf(sym("verbo+ed", "passado dos verbos regulares"), sym("irregular", "passado com forma própria, como went"),
            sym("did", "auxiliar do passado para perguntar"), sym("didn't", "did not, negativa no passado")),
        listOf(
            step("""I\ work\ →\ I\ worked""", "Regular: + ed, igual para todos."),
            step("""I\ go\ →\ I\ went""", "Irregular: precisa decorar."),
            step("""Did\ you\ go?""", "Pergunta: did + verbo puro."),
            step("""I\ didn't\ go.""", "Negativa: didn't + verbo puro.")
        ),
        listOf(
            case(Scene.Travel, "Contando uma viagem", "Narrando o que você fez nas férias.",
                "We went to the beach and ate seafood.",
                fill("Complete:", "Last summer we ___ to Salvador.", "went", "Go é irregular: went.",
                    listOf("Last summer: passado.", "Go → went."),
                    "goed" to "Go é irregular. O passado é went.", "go" to "Last summer pede passado.")),
            case(Scene.Work, "Reunião de status", "Dizendo o que você terminou.",
                "I finished the report. Did you send the email?",
                fill("Complete a pergunta:", "___ you send the email?", "Did", "Pergunta no passado: did.",
                    listOf("A pergunta é sobre algo já feito.", "Passado → did."),
                    "Do" to "Do é presente.", "Were" to "Send é verbo de ação: use did."))
        ),
        iso("Qual está certa?", "Did she call you?",
            "Did já marca o passado. O verbo fica puro.",
            "Did she called you?" to "Depois de did, o verbo fica sem -ed.",
            "Does she called you?" to "Does é presente e called não vai depois de auxiliar."),
        listOf(say("I went to the gym.", "Fui à academia.", "ai uént tu dâ djím"),
            say("Did you sleep well?", "Você dormiu bem?", "did iú slíp uél"),
            say("I didn't see it.", "Eu não vi.", "ai dídant sí it"),
            say("We had a great time.", "Nos divertimos muito.", "uí rréd a grêit táim"),
            say("She bought a new phone.", "Ela comprou um celular novo.", "xí bót a niú fôun"))
    ) { a, b ->
        val w = who(a, b)
        data class P(val base: String, val past: String, val comp: String, val irregular: Boolean)
        val verbs = listOf(P("go", "went", "to the beach", true), P("eat", "ate", "pizza", true), P("see", "saw", "a movie", true),
            P("have", "had", "a great day", true), P("buy", "bought", "new shoes", true), P("make", "made", "a cake", true),
            P("work", "worked", "a lot", false), P("play", "played", "video games", false), P("study", "studied", "for the test", false),
            P("watch", "watched", "a series", false))
        val v = verbs[(a * 2 + b) % verbs.size]
        if (b % 3 == 0) {
            fill("Negue no passado (${v.base}):", "Last weekend, ${w.inside()} ___ ${v.comp}.", "didn't ${v.base}",
                "Negativa no passado: didn't + verbo puro.",
                listOf("Passado negativo: didn't.", "Depois de didn't: ${v.base}."),
                "didn't ${v.past}" to "O passado já está no didn't. O verbo fica puro.",
                "${if (w.third) "doesn't" else "don't"} ${v.base}" to "Last weekend pede passado: didn't.")
        } else {
            fill("Complete no passado (${v.base}):", "Last weekend, ${w.inside()} ___ ${v.comp}.", v.past,
                if (v.irregular) "${v.base} é irregular: ${v.past}." else "${v.base} é regular: ${v.past}.",
                listOf("Last weekend: passado.", "${v.base} → ${v.past}."),
                (if (w.third) third(v.base) else v.base) to "Esse é o presente. Last weekend pede passado.",
                (if (v.irregular) v.base + (if (v.base.endsWith("e")) "d" else "ed") else "did ${v.base}") to if (v.irregular) "${v.base} é irregular: ${v.past}." else "Na afirmativa não se usa did.")
        }
    },

    english("en-future", "Sujeito+will+verbo\\quad Sujeito+am/is/are+going\\ to+verbo", "will + verbo · am/is/are + going to + verbo",
        "Plano decidido: going to. Decisão na hora, promessa, previsão: will.",
        listOf(sym("will", "vou, vai, para decisões na hora e promessas"), sym("going to", "vou, vai, para planos já decididos"),
            sym("won't", "will not, não vou"), sym("verbo", "a ação na forma básica")),
        listOf(
            step("""I\ will\ help\ you.""", "Decidi agora: will."),
            step("""I\ am\ going\ to\ travel.""", "Já estava planejado: going to, com o to be certo."),
            step("""I'll\quad I'm\ gonna""", "Na fala: I'll e gonna."),
            step("""Will\ you\ come?""", "Pergunta: will na frente.")
        ),
        listOf(
            case(Scene.Phone, "Promessa por mensagem", "Você promete mandar um arquivo amanhã.",
                "I'll send it tomorrow, I promise.",
                fill("Complete (promessa):", "Don't worry, I ___ send it tomorrow.", "will", "Promessa: will.",
                    listOf("É uma promessa feita agora.", "Promessa → will."),
                    "will to" to "Will não usa to.", "am going" to "Falta o to, e promessa combina com will.")),
            case(Scene.Travel, "Planos de férias", "Contando a viagem que você já comprou.",
                "We're going to visit Chile in July.",
                fill("Complete (plano decidido):", "We ___ visit Chile in July.", "are going to", "Plano decidido: are going to.",
                    listOf("A viagem já está planejada.", "We → are going to."),
                    "going to" to "Falta o are.", "will to" to "Will não usa to."))
        ),
        iso("A campainha toca e você decide: \"Eu atendo!\"", "I'll get it!",
            "Decisão no momento: will.",
            "I'm going to get it!" to "Going to é para planos feitos antes.",
            "I get it!" to "Isso é presente. Para decisão na hora, use will.")
        ,
        listOf(say("I'll call you later.", "Te ligo mais tarde.", "áil cól iú lêitar"),
            say("I'm going to study tonight.", "Vou estudar hoje à noite.", "áim gôuin tu stâdi tunáit"),
            say("It will rain tomorrow.", "Vai chover amanhã.", "it uíl rrêin tumórou"),
            say("I won't forget.", "Não vou esquecer.", "ai uônt forguét"))
    ) { a, b ->
        val w = who(a, b)
        val verbs = listOf("visit my grandma", "buy a car", "move to Canada", "start a course", "travel", "watch the game", "learn Spanish", "cook dinner")
        val verb = verbs[(a + b * 2) % verbs.size]
        if (b % 2 == 0) {
            fill("Complete (plano já decidido):", "${w.subject} ___ $verb next week.", "${w.be} going to",
                "Plano decidido: ${w.be} going to.",
                listOf("É um plano feito antes.", "${beRule(w)} Depois: going to."),
                "going to" to "Falta o to be (${w.be}).",
                "will to" to "Will não usa to.")
        } else {
            fill("Complete (promessa feita agora):", "Don't worry, ${w.inside()} ___ $verb.", "will",
                "Promessa ou decisão na hora: will, igual para todos.",
                listOf("É uma promessa feita agora.", "Will + verbo puro."),
                "will to" to "Will não usa to.",
                "wills" to "Will nunca ganha -s.")
        }
    }
)
