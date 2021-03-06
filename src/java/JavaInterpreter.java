import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

/**
 *
 * @author jenilyn
 */
public class JavaInterpreter {
    
    public static HashMap hashMap = new HashMap();
    public String javaConverted = "";
    
    public JavaInterpreter (String srcCode, String className){
//        String srcCode = readTxtFile("SampleCode.txt");
        
        srcCode = srcCode.replace("}", " };");
        srcCode = srcCode.replace("{", " {;");
        srcCode = srcCode.replace("(", " ( ");
        srcCode = srcCode.replace(")", " ) ");
        
        srcCode = srcCode.replace("=call ", "= call ");
        srcCode = srcCode.replace(";call ", "; call ");
        srcCode = srcCode.replace(" call ", " ");
        
        //For arrays
        srcCode = srcCode.replace("[", ";[");
        
        //Print
        srcCode = srcCode.replace("print ", "return ");
            
        String javaCode = "";
        List<String> statements = Arrays.asList(srcCode.split(";+"));
        
        for(String stmt: statements){
            stmt = stmt.trim();
            if(isDeclarationStmt(stmt)){
                javaCode += convertDeclarationStmt(stmt);
            }else if (isCondition(stmt)){
                javaCode += convertConditionStmt(stmt);
            }else if (isFunctionDefinition(stmt)){
               javaCode += convertFunctionDefinitionStmt(stmt);
            }else if (isForEach(stmt)){
                javaCode += convertForEachStmt(stmt);
            }
            else if(isArray(stmt)){
                javaCode += convertArray(stmt);
            }
            else if (stmt.equals("}")){
                javaCode += "}";
            }else if(stmt.length()>0 && stmt.charAt(stmt.length()-1)=='='){
                javaCode += stmt;
            }else if(!stmt.equals("")){
                javaCode += stmt+";";
            }
            
            //Only add new line is current statement is not empty
            javaCode += stmt.equals("")? "": "\n";
        }
        
        String pkg = "";
        
        String head = "import java.util.*;\n"
                    + "import java.lang.*;\n"
                    + "import java.io.*;\n"
                    + "import java.net.*;\n"
                    + "import java.awt.event.*;\n"
                    + "import java.text.*;\n"
                    + "import java.util.regex.*;\n"
                    + "import java.io.*;\n"
                    + "\n\npublic class "+className+ "{\n\n"
                    + "public static String main (String args[]){\n\n";
        
        String mainClose = "\n\n}\n\n";
        
        String builtInFunctions = "public static String getContent(String linkURL)\n" +
                                "{\n" +
                                "    URL url = null;\n" +
                                "    InputStream is = null;\n" +
                                "    BufferedReader reader = null;\n" +
                                "    String line = null;\n" +
                                "    String content =\"\";\n" +
                                "    try\n" +
                                "    {\n" +
                                "        System.out.println(\"Link URL: \"+linkURL);\n" +
                                "        url = new URL(linkURL);\n" +
                                 "        throw new Exception();\n" +
                                "        is = url.openConnection().getInputStream();\n" +
                                "        reader = new BufferedReader( new InputStreamReader( is ));\n" +
                                "\n" +
                                "        while( ( line = reader.readLine() ) != null )  \n" +
                                "        {\n" +
                                "           content += line;\n" +
                                "        }\n" +
                                "        reader.close();\n" +
                                "    }\n" +
                                "    catch(Exception e)\n" +
                                "    {\n" +
                                "System.out.println(\"NAAY SAYUP BESHIEEEE\");"+
                                "        System.err.println(e.getMessage());\n" +
                                "    }    \n" +
                                "    if(content.length()>1000){\n" +
                                "      content = content.substring(0,999);\n" +
                                "     }" +
                                "    return  content;\n" +
                                "}"
			+ "\n\n/*-------------------------- Sentiments Analysis ------------------------------------------------- */\n\n"
                        + "public static final String[] NEGATIVE_WORDS = {\"2-faced\",\"2-faces\",\"abnormal\",\"abolish\",\"abominable\",\"abominably\",\"abominate\",\"abomination\",\"abort\",\"aborted\",\"aborts\",\"abrade\",\"abrasive\",\"abrupt\",\"abruptly\",\"abscond\",\"absence\",\"absent-minded\",\"absentee\",\"absurd\",\"absurdity\",\"absurdly\",\"absurdness\",\"abuse\",\"abused\",\"abuses\",\"abusive\",\"abysmal\",\"abysmally\",\"abyss\",\"accidental\",\"accost\",\"accursed\",\"accusation\",\"accusations\",\"accuse\",\"accuses\",\"accusing\",\"accusingly\",\"acerbate\",\"acerbic\",\"acerbically\",\"ache\",\"ached\",\"aches\",\"achey\",\"aching\",\"acrid\",\"acridly\",\"acridness\",\"acrimonious\",\"acrimoniously\",\"acrimony\",\"adamant\",\"adamantly\",\"addict\",\"addicted\",\"addicting\",\"addicts\",\"admonish\",\"admonisher\",\"admonishingly\",\"admonishment\",\"admonition\",\"adulterate\",\"adulterated\",\"adulteration\",\"adulterier\",\"adversarial\",\"adversary\",\"adverse\",\"adversity\",\"afflict\",\"affliction\",\"afflictive\",\"affront\",\"afraid\",\"aggravate\",\"aggravating\",\"aggravation\",\"aggression\",\"aggressive\",\"aggressiveness\",\"aggressor\",\"aggrieve\",\"aggrieved\",\"aggrivation\",\"aghast\",\"agonies\",\"agonize\",\"agonizing\",\"agonizingly\",\"agony\",\"aground\",\"ail\",\"ailing\",\"ailment\",\"aimless\",\"alarm\",\"alarmed\",\"alarming\",\"alarmingly\",\"alienate\",\"alienated\",\"alienation\",\"allegation\",\"allegations\",\"allege\",\"allergic\",\"allergies\",\"allergy\",\"aloof\",\"altercation\",\"ambiguity\",\"ambiguous\",\"ambivalence\",\"ambivalent\",\"ambush\",\"amiss\",\"amputate\",\"anarchism\",\"anarchist\",\"anarchistic\",\"anarchy\",\"anemic\",\"anger\",\"angrily\",\"angriness\",\"angry\",\"anguish\",\"animosity\",\"annihilate\",\"annihilation\",\"annoy\",\"annoyance\",\"annoyances\",\"annoyed\",\"annoying\",\"annoyingly\",\"annoys\",\"anomalous\",\"anomaly\",\"antagonism\",\"antagonist\",\"antagonistic\",\"antagonize\",\"anti-\",\"anti-american\",\"anti-israeli\",\"anti-occupation\",\"anti-proliferation\",\"anti-semites\",\"anti-social\",\"anti-us\",\"anti-white\",\"antipathy\",\"antiquated\",\"antithetical\",\"anxieties\",\"anxiety\",\"anxious\",\"anxiously\",\"anxiousness\",\"apathetic\",\"apathetically\",\"apathy\",\"apocalypse\",\"apocalyptic\",\"apologist\",\"apologists\",\"appal\",\"appall\",\"appalled\",\"appalling\",\"appallingly\",\"apprehension\",\"apprehensions\",\"apprehensive\",\"apprehensively\",\"arbitrary\",\"arcane\",\"archaic\",\"arduous\",\"arduously\",\"argumentative\",\"arrogance\",\"arrogant\",\"arrogantly\",\"ashamed\",\"asinine\",\"asininely\",\"asinininity\",\"askance\",\"asperse\",\"aspersion\",\"aspersions\",\"assail\",\"assassin\",\"assassinate\",\"assault\",\"assult\",\"astray\",\"asunder\",\"atrocious\",\"atrocities\",\"atrocity\",\"atrophy\",\"attack\",\"attacks\",\"audacious\",\"audaciously\",\"audaciousness\",\"audacity\",\"audiciously\",\"austere\",\"authoritarian\",\"autocrat\",\"autocratic\",\"avalanche\",\"avarice\",\"avaricious\",\"avariciously\",\"avenge\",\"averse\",\"aversion\",\"aweful\",\"awful\",\"awfully\",\"awfulness\",\"awkward\",\"awkwardness\",\"ax\",\"babble\",\"back-logged\",\"back-wood\",\"back-woods\",\"backache\",\"backaches\",\"backaching\",\"backbite\",\"backbiting\",\"backward\",\"backwardness\",\"backwood\",\"backwoods\",\"bad\",\"badly\",\"baffle\",\"baffled\",\"bafflement\",\"baffling\",\"bait\",\"balk\",\"banal\",\"banalize\",\"bane\",\"banish\",\"banishment\",\"bankrupt\",\"barbarian\",\"barbaric\",\"barbarically\",\"barbarity\",\"barbarous\",\"barbarously\",\"barren\",\"baseless\",\"bash\",\"bashed\",\"bashful\",\"bashing\",\"bastard\",\"bastards\",\"battered\",\"battering\",\"batty\",\"bearish\",\"beastly\",\"bedlam\",\"bedlamite\",\"befoul\",\"beg\",\"beggar\",\"beggarly\",\"begging\",\"beguile\",\"belabor\",\"belated\",\"beleaguer\",\"belie\",\"belittle\",\"belittled\",\"belittling\",\"bellicose\",\"belligerence\",\"belligerent\",\"belligerently\",\"bemoan\",\"bemoaning\",\"bemused\",\"bent\",\"berate\",\"bereave\",\"bereavement\",\"bereft\",\"berserk\",\"beseech\",\"beset\",\"besiege\",\"besmirch\",\"bestial\",\"betray\",\"betrayal\",\"betrayals\",\"betrayer\",\"betraying\",\"betrays\",\"bewail\",\"beware\",\"bewilder\",\"bewildered\",\"bewildering\",\"bewilderingly\",\"bewilderment\",\"bewitch\",\"bias\",\"biased\",\"biases\",\"bicker\",\"bickering\",\"bid-rigging\",\"bigotries\",\"bigotry\",\"bitch\",\"bitchy\",\"biting\",\"bitingly\",\"bitter\",\"bitterly\",\"bitterness\",\"bizarre\",\"blab\",\"blabber\",\"blackmail\",\"blah\",\"blame\",\"blameworthy\",\"bland\",\"blandish\",\"blaspheme\",\"blasphemous\",\"blasphemy\",\"blasted\",\"blatant\",\"blatantly\",\"blather\",\"bleak\",\"bleakly\",\"bleakness\",\"bleed\",\"bleeding\",\"bleeds\",\"blemish\",\"blind\",\"blinding\",\"blindingly\",\"blindside\",\"blister\",\"blistering\",\"bloated\",\"blockage\",\"blockhead\",\"bloodshed\",\"bloodthirsty\",\"bloody\",\"blotchy\",\"blow\",\"blunder\",\"blundering\",\"blunders\",\"blunt\",\"blur\",\"bluring\",\"blurred\",\"blurring\",\"blurry\",\"blurs\",\"blurt\",\"boastful\",\"boggle\",\"bogus\",\"boil\",\"boiling\",\"boisterous\",\"bomb\",\"bombard\",\"bombardment\",\"bombastic\",\"bondage\",\"bonkers\",\"bore\",\"bored\",\"boredom\",\"bores\",\"boring\",\"botch\",\"bother\",\"bothered\",\"bothering\",\"bothers\",\"bothersome\",\"bowdlerize\",\"boycott\",\"braggart\",\"bragger\",\"brainless\",\"brainwash\",\"brash\",\"brashly\",\"brashness\",\"brat\",\"bravado\",\"brazen\",\"brazenly\",\"brazenness\",\"breach\",\"break\",\"break-up\",\"break-ups\",\"breakdown\",\"breaking\",\"breaks\",\"breakup\",\"breakups\",\"bribery\",\"brimstone\",\"bristle\",\"brittle\",\"broke\",\"broken\",\"broken-hearted\",\"brood\",\"browbeat\",\"bruise\",\"bruised\",\"bruises\",\"bruising\",\"brusque\",\"brutal\",\"brutalising\",\"brutalities\",\"brutality\",\"brutalize\",\"brutalizing\",\"brutally\",\"brute\",\"brutish\",\"bs\",\"buckle\",\"bug\",\"bugging\",\"buggy\",\"bugs\",\"bulkier\",\"bulkiness\",\"bulky\",\"bulkyness\",\"bull****\",\"bull----\",\"bullies\",\"bullshit\",\"bullshyt\",\"bully\",\"bullying\",\"bullyingly\",\"bum\",\"bump\",\"bumped\",\"bumping\",\"bumpping\",\"bumps\",\"bumpy\",\"bungle\",\"bungler\",\"bungling\",\"bunk\",\"burden\",\"burdensome\",\"burdensomely\",\"burn\",\"burned\",\"burning\",\"burns\",\"bust\",\"busts\",\"busybody\",\"butcher\",\"butchery\",\"buzzing\",\"byzantine\",\"cackle\",\"calamities\",\"calamitous\",\"calamitously\",\"calamity\",\"callous\",\"calumniate\",\"calumniation\",\"calumnies\",\"calumnious\",\"calumniously\",\"calumny\",\"cancer\",\"cancerous\",\"cannibal\",\"cannibalize\",\"capitulate\",\"capricious\",\"capriciously\",\"capriciousness\",\"capsize\",\"careless\",\"carelessness\",\"caricature\",\"carnage\",\"carp\",\"cartoonish\",\"cash-strapped\",\"castigate\",\"castrated\",\"casualty\",\"cataclysm\",\"cataclysmal\",\"cataclysmic\",\"cataclysmically\",\"catastrophe\",\"catastrophes\",\"catastrophic\",\"catastrophically\",\"catastrophies\",\"caustic\",\"caustically\",\"cautionary\",\"cave\",\"censure\",\"chafe\",\"chaff\",\"chagrin\",\"challenging\",\"chaos\",\"chaotic\",\"chasten\",\"chastise\",\"chastisement\",\"chatter\",\"chatterbox\",\"cheap\",\"cheapen\",\"cheaply\",\"cheat\",\"cheated\",\"cheater\",\"cheating\",\"cheats\",\"checkered\",\"cheerless\",\"cheesy\",\"chide\",\"childish\",\"chill\",\"chilly\",\"chintzy\",\"choke\",\"choleric\",\"choppy\",\"chore\",\"chronic\",\"chunky\",\"clamor\",\"clamorous\",\"clash\",\"cliche\",\"cliched\",\"clique\",\"clog\",\"clogged\",\"clogs\",\"cloud\",\"clouding\",\"cloudy\",\"clueless\",\"clumsy\",\"clunky\",\"coarse\",\"cocky\",\"coerce\",\"coercion\",\"coercive\",\"cold\",\"coldly\",\"collapse\",\"collude\",\"collusion\",\"combative\",\"combust\",\"comical\",\"commiserate\",\"commonplace\",\"commotion\",\"commotions\",\"complacent\",\"complain\",\"complained\",\"complaining\",\"complains\",\"complaint\",\"complaints\",\"complex\",\"complicated\",\"complication\",\"complicit\",\"compulsion\",\"compulsive\",\"concede\",\"conceded\",\"conceit\",\"conceited\",\"concen\",\"concens\",\"concern\",\"concerned\",\"concerns\",\"concession\",\"concessions\",\"condemn\",\"condemnable\",\"condemnation\",\"condemned\",\"condemns\",\"condescend\",\"condescending\",\"condescendingly\",\"condescension\",\"confess\",\"confession\",\"confessions\",\"confined\",\"conflict\",\"conflicted\",\"conflicting\",\"conflicts\",\"confound\",\"confounded\",\"confounding\",\"confront\",\"confrontation\",\"confrontational\",\"confuse\",\"confused\",\"confuses\",\"confusing\",\"confusion\",\"confusions\",\"congested\",\"congestion\",\"cons\",\"conscons\",\"conservative\",\"conspicuous\",\"conspicuously\",\"conspiracies\",\"conspiracy\",\"conspirator\",\"conspiratorial\",\"conspire\",\"consternation\",\"contagious\",\"contaminate\",\"contaminated\",\"contaminates\",\"contaminating\",\"contamination\",\"contempt\",\"contemptible\",\"contemptuous\",\"contemptuously\",\"contend\",\"contention\",\"contentious\",\"contort\",\"contortions\",\"contradict\",\"contradiction\",\"contradictory\",\"contrariness\",\"contravene\",\"contrive\",\"contrived\",\"controversial\",\"controversy\",\"convoluted\",\"corrode\",\"corrosion\",\"corrosions\",\"corrosive\",\"corrupt\",\"corrupted\",\"corrupting\",\"corruption\",\"corrupts\",\"corruptted\",\"costlier\",\"costly\",\"counter-productive\",\"counterproductive\",\"coupists\",\"covetous\",\"coward\",\"cowardly\",\"crabby\",\"crack\",\"cracked\",\"cracks\",\"craftily\",\"craftly\",\"crafty\",\"cramp\",\"cramped\",\"cramping\",\"cranky\",\"crap\",\"crappy\",\"craps\",\"crash\",\"crashed\",\"crashes\",\"crashing\",\"crass\",\"craven\",\"cravenly\",\"craze\",\"crazily\",\"craziness\",\"crazy\",\"creak\",\"creaking\",\"creaks\",\"credulous\",\"creep\",\"creeping\",\"creeps\",\"creepy\",\"crept\",\"crime\",\"criminal\",\"cringe\",\"cringed\",\"cringes\",\"cripple\",\"crippled\",\"cripples\",\"crippling\",\"crisis\",\"critic\",\"critical\",\"criticism\",\"criticisms\",\"criticize\",\"criticized\",\"criticizing\",\"critics\",\"cronyism\",\"crook\",\"crooked\",\"crooks\",\"crowded\",\"crowdedness\",\"crude\",\"cruel\",\"crueler\",\"cruelest\",\"cruelly\",\"cruelness\",\"cruelties\",\"cruelty\",\"crumble\",\"crumbling\",\"crummy\",\"crumple\",\"crumpled\",\"crumples\",\"crush\",\"crushed\",\"crushing\",\"cry\",\"culpable\",\"culprit\",\"cumbersome\",\"cunt\",\"cunts\",\"cuplrit\",\"curse\",\"cursed\",\"curses\",\"curt\",\"cuss\",\"cussed\",\"cutthroat\",\"cynical\",\"cynicism\",\"d*mn\",\"damage\",\"damaged\",\"damages\",\"damaging\",\"damn\",\"damnable\",\"damnably\",\"damnation\",\"damned\",\"damning\",\"damper\",\"danger\",\"dangerous\",\"dangerousness\",\"dark\",\"darken\",\"darkened\",\"darker\",\"darkness\",\"dastard\",\"dastardly\",\"daunt\",\"daunting\",\"dauntingly\",\"dawdle\",\"daze\",\"dazed\",\"dead\",\"deadbeat\",\"deadlock\",\"deadly\",\"deadweight\",\"deaf\",\"dearth\",\"death\",\"debacle\",\"debase\",\"debasement\",\"debaser\",\"debatable\",\"debauch\",\"debaucher\",\"debauchery\",\"debilitate\",\"debilitating\",\"debility\",\"debt\",\"debts\",\"decadence\",\"decadent\",\"decay\",\"decayed\",\"deceit\",\"deceitful\",\"deceitfully\",\"deceitfulness\",\"deceive\",\"deceiver\",\"deceivers\",\"deceiving\",\"deception\",\"deceptive\",\"deceptively\",\"declaim\",\"decline\",\"declines\",\"declining\",\"decrement\",\"decrepit\",\"decrepitude\",\"decry\",\"defamation\",\"defamations\",\"defamatory\",\"defame\",\"defect\",\"defective\",\"defects\",\"defensive\",\"defiance\",\"defiant\",\"defiantly\",\"deficiencies\",\"deficiency\",\"deficient\",\"defile\",\"defiler\",\"deform\",\"deformed\",\"defrauding\",\"defunct\",\"defy\",\"degenerate\",\"degenerately\",\"degeneration\",\"degradation\",\"degrade\",\"degrading\",\"degradingly\",\"dehumanization\",\"dehumanize\",\"deign\",\"deject\",\"dejected\",\"dejectedly\",\"dejection\",\"delay\",\"delayed\",\"delaying\",\"delays\",\"delinquency\",\"delinquent\",\"delirious\",\"delirium\",\"delude\",\"deluded\",\"deluge\",\"delusion\",\"delusional\",\"delusions\",\"demean\",\"demeaning\",\"demise\",\"demolish\",\"demolisher\",\"demon\",\"demonic\",\"demonize\",\"demonized\",\"demonizes\",\"demonizing\",\"demoralize\",\"demoralizing\",\"demoralizingly\",\"denial\",\"denied\",\"denies\",\"denigrate\",\"denounce\",\"dense\",\"dent\",\"dented\",\"dents\",\"denunciate\",\"denunciation\",\"denunciations\",\"deny\",\"denying\",\"deplete\",\"deplorable\",\"deplorably\",\"deplore\",\"deploring\",\"deploringly\",\"deprave\",\"depraved\",\"depravedly\",\"deprecate\",\"depress\",\"depressed\",\"depressing\",\"depressingly\",\"depression\",\"depressions\",\"deprive\",\"deprived\",\"deride\",\"derision\",\"derisive\",\"derisively\",\"derisiveness\",\"derogatory\",\"desecrate\",\"desert\",\"desertion\",\"desiccate\",\"desiccated\",\"desititute\",\"desolate\",\"desolately\",\"desolation\",\"despair\",\"despairing\",\"despairingly\",\"desperate\",\"desperately\",\"desperation\",\"despicable\",\"despicably\",\"despise\",\"despised\",\"despoil\",\"despoiler\",\"despondence\",\"despondency\",\"despondent\",\"despondently\",\"despot\",\"despotic\",\"despotism\",\"destabilisation\",\"destains\",\"destitute\",\"destitution\",\"destroy\",\"destroyer\",\"destruction\",\"destructive\",\"desultory\",\"deter\",\"deteriorate\",\"deteriorating\",\"deterioration\",\"deterrent\",\"detest\",\"detestable\",\"detestably\",\"detested\",\"detesting\",\"detests\",\"detract\",\"detracted\",\"detracting\",\"detraction\",\"detracts\",\"detriment\",\"detrimental\",\"devastate\",\"devastated\",\"devastates\",\"devastating\",\"devastatingly\",\"devastation\",\"deviate\",\"deviation\",\"devil\",\"devilish\",\"devilishly\",\"devilment\",\"devilry\",\"devious\",\"deviously\",\"deviousness\",\"devoid\",\"diabolic\",\"diabolical\",\"diabolically\",\"diametrically\",\"diappointed\",\"diatribe\",\"diatribes\",\"dick\",\"dictator\",\"dictatorial\",\"die\",\"die-hard\",\"died\",\"dies\",\"difficult\",\"difficulties\",\"difficulty\",\"diffidence\",\"dilapidated\",\"dilemma\",\"dilly-dally\",\"dim\",\"dimmer\",\"din\",\"ding\",\"dings\",\"dinky\",\"dire\",\"direly\",\"direness\",\"dirt\",\"dirtbag\",\"dirtbags\",\"dirts\",\"dirty\",\"disable\",\"disabled\",\"disaccord\",\"disadvantage\",\"disadvantaged\",\"disadvantageous\",\"disadvantages\",\"disaffect\",\"disaffected\",\"disaffirm\",\"disagree\",\"disagreeable\",\"disagreeably\",\"disagreed\",\"disagreeing\",\"disagreement\",\"disagrees\",\"disallow\",\"disapointed\",\"disapointing\",\"disapointment\",\"disappoint\",\"disappointed\",\"disappointing\",\"disappointingly\",\"disappointment\",\"disappointments\",\"disappoints\",\"disapprobation\",\"disapproval\",\"disapprove\",\"disapproving\",\"disarm\",\"disarray\",\"disaster\",\"disasterous\",\"disastrous\",\"disastrously\",\"disavow\",\"disavowal\",\"disbelief\",\"disbelieve\",\"disbeliever\",\"disclaim\",\"discombobulate\",\"discomfit\",\"discomfititure\",\"discomfort\",\"discompose\",\"disconcert\",\"disconcerted\",\"disconcerting\",\"disconcertingly\",\"disconsolate\",\"disconsolately\",\"disconsolation\",\"discontent\",\"discontented\",\"discontentedly\",\"discontinued\",\"discontinuity\",\"discontinuous\",\"discord\",\"discordance\",\"discordant\",\"discountenance\",\"discourage\",\"discouragement\",\"discouraging\",\"discouragingly\",\"discourteous\",\"discourteously\",\"discoutinous\",\"discredit\",\"discrepant\",\"discriminate\",\"discrimination\",\"discriminatory\",\"disdain\",\"disdained\",\"disdainful\",\"disdainfully\",\"disfavor\",\"disgrace\",\"disgraced\",\"disgraceful\",\"disgracefully\",\"disgruntle\",\"disgruntled\",\"disgust\",\"disgusted\",\"disgustedly\",\"disgustful\",\"disgustfully\",\"disgusting\",\"disgustingly\",\"dishearten\",\"disheartening\",\"dishearteningly\",\"dishonest\",\"dishonestly\",\"dishonesty\",\"dishonor\",\"dishonorable\",\"dishonorablely\",\"disillusion\",\"disillusioned\",\"disillusionment\",\"disillusions\",\"disinclination\",\"disinclined\",\"disingenuous\",\"disingenuously\",\"disintegrate\",\"disintegrated\",\"disintegrates\",\"disintegration\",\"disinterest\",\"disinterested\",\"dislike\",\"disliked\",\"dislikes\",\"disliking\",\"dislocated\",\"disloyal\",\"disloyalty\",\"dismal\",\"dismally\",\"dismalness\",\"dismay\",\"dismayed\",\"dismaying\",\"dismayingly\",\"dismissive\",\"dismissively\",\"disobedience\",\"disobedient\",\"disobey\",\"disoobedient\",\"disorder\",\"disordered\",\"disorderly\",\"disorganized\",\"disorient\",\"disoriented\",\"disown\",\"disparage\",\"disparaging\",\"disparagingly\",\"dispensable\",\"dispirit\",\"dispirited\",\"dispiritedly\",\"dispiriting\",\"displace\",\"displaced\",\"displease\",\"displeased\",\"displeasing\",\"displeasure\",\"disproportionate\",\"disprove\",\"disputable\",\"dispute\",\"disputed\",\"disquiet\",\"disquieting\",\"disquietingly\",\"disquietude\",\"disregard\",\"disregardful\",\"disreputable\",\"disrepute\",\"disrespect\",\"disrespectable\",\"disrespectablity\",\"disrespectful\",\"disrespectfully\",\"disrespectfulness\",\"disrespecting\",\"disrupt\",\"disruption\",\"disruptive\",\"diss\",\"dissapointed\",\"dissappointed\",\"dissappointing\",\"dissatisfaction\",\"dissatisfactory\",\"dissatisfied\",\"dissatisfies\",\"dissatisfy\",\"dissatisfying\",\"dissed\",\"dissemble\",\"dissembler\",\"dissension\",\"dissent\",\"dissenter\",\"dissention\",\"disservice\",\"disses\",\"dissidence\",\"dissident\",\"dissidents\",\"dissing\",\"dissocial\",\"dissolute\",\"dissolution\",\"dissonance\",\"dissonant\",\"dissonantly\",\"dissuade\",\"dissuasive\",\"distains\",\"distaste\",\"distasteful\",\"distastefully\",\"distort\",\"distorted\",\"distortion\",\"distorts\",\"distract\",\"distracting\",\"distraction\",\"distraught\",\"distraughtly\",\"distraughtness\",\"distress\",\"distressed\",\"distressing\",\"distressingly\",\"distrust\",\"distrustful\",\"distrusting\",\"disturb\",\"disturbance\",\"disturbed\",\"disturbing\",\"disturbingly\",\"disunity\",\"disvalue\",\"divergent\",\"divisive\",\"divisively\",\"divisiveness\",\"dizzing\",\"dizzingly\",\"dizzy\",\"doddering\",\"dodgey\",\"dogged\",\"doggedly\",\"dogmatic\",\"doldrums\",\"domineer\",\"domineering\",\"donside\",\"doom\",\"doomed\",\"doomsday\",\"dope\",\"doubt\",\"doubtful\",\"doubtfully\",\"doubts\",\"douchbag\",\"douchebag\",\"douchebags\",\"downbeat\",\"downcast\",\"downer\",\"downfall\",\"downfallen\",\"downgrade\",\"downhearted\",\"downheartedly\",\"downhill\",\"downside\",\"downsides\",\"downturn\",\"downturns\",\"drab\",\"draconian\",\"draconic\",\"drag\",\"dragged\",\"dragging\",\"dragoon\",\"drags\",\"drain\",\"drained\",\"draining\",\"drains\",\"drastic\",\"drastically\",\"drawback\",\"drawbacks\",\"dread\",\"dreadful\",\"dreadfully\",\"dreadfulness\",\"dreary\",\"dripped\",\"dripping\",\"drippy\",\"drips\",\"drones\",\"droop\",\"droops\",\"drop-out\",\"drop-outs\",\"dropout\",\"dropouts\",\"drought\",\"drowning\",\"drunk\",\"drunkard\",\"drunken\",\"dubious\",\"dubiously\",\"dubitable\",\"dud\",\"dull\",\"dullard\",\"dumb\",\"dumbfound\",\"dump\",\"dumped\",\"dumping\",\"dumps\",\"dunce\",\"dungeon\",\"dungeons\",\"dupe\",\"dust\",\"dusty\",\"dwindling\",\"dying\",\"earsplitting\",\"eccentric\",\"eccentricity\",\"effigy\",\"effrontery\",\"egocentric\",\"egomania\",\"egotism\",\"egotistical\",\"egotistically\",\"egregious\",\"egregiously\",\"election-rigger\",\"elimination\",\"emaciated\",\"emasculate\",\"embarrass\",\"embarrassing\",\"embarrassingly\",\"embarrassment\",\"embattled\",\"embroil\",\"embroiled\",\"embroilment\",\"emergency\",\"emphatic\",\"emphatically\",\"emptiness\",\"encroach\",\"encroachment\",\"endanger\",\"enemies\",\"enemy\",\"enervate\",\"enfeeble\",\"enflame\",\"engulf\",\"enjoin\",\"enmity\",\"enrage\",\"enraged\",\"enraging\",\"enslave\",\"entangle\",\"entanglement\",\"entrap\",\"entrapment\",\"envious\",\"enviously\",\"enviousness\",\"epidemic\",\"equivocal\",\"erase\",\"erode\",\"erodes\",\"erosion\",\"err\",\"errant\",\"erratic\",\"erratically\",\"erroneous\",\"erroneously\",\"error\",\"errors\",\"eruptions\",\"escapade\",\"eschew\",\"estranged\",\"evade\",\"evasion\",\"evasive\",\"evil\",\"evildoer\",\"evils\",\"eviscerate\",\"exacerbate\",\"exagerate\",\"exagerated\",\"exagerates\",\"exaggerate\",\"exaggeration\",\"exasperate\",\"exasperated\",\"exasperating\",\"exasperatingly\",\"exasperation\",\"excessive\",\"excessively\",\"exclusion\",\"excoriate\",\"excruciating\",\"excruciatingly\",\"excuse\",\"excuses\",\"execrate\",\"exhaust\",\"exhausted\",\"exhaustion\",\"exhausts\",\"exhorbitant\",\"exhort\",\"exile\",\"exorbitant\",\"exorbitantance\",\"exorbitantly\",\"expel\",\"expensive\",\"expire\",\"expired\",\"explode\",\"exploit\",\"exploitation\",\"explosive\",\"expropriate\",\"expropriation\",\"expulse\",\"expunge\",\"exterminate\",\"extermination\",\"extinguish\",\"extort\",\"extortion\",\"extraneous\",\"extravagance\",\"extravagant\",\"extravagantly\",\"extremism\",\"extremist\",\"extremists\",\"eyesore\",\"f**k\",\"fabricate\",\"fabrication\",\"facetious\",\"facetiously\",\"fail\",\"failed\",\"failing\",\"fails\",\"failure\",\"failures\",\"faint\",\"fainthearted\",\"faithless\",\"fake\",\"fall\",\"fallacies\",\"fallacious\",\"fallaciously\",\"fallaciousness\",\"fallacy\",\"fallen\",\"falling\",\"fallout\",\"falls\",\"false\",\"falsehood\",\"falsely\",\"falsify\",\"falter\",\"faltered\",\"famine\",\"famished\",\"fanatic\",\"fanatical\",\"fanatically\",\"fanaticism\",\"fanatics\",\"fanciful\",\"far-fetched\",\"farce\",\"farcical\",\"farcical-yet-provocative\",\"farcically\",\"farfetched\",\"fascism\",\"fascist\",\"fastidious\",\"fastidiously\",\"fastuous\",\"fat\",\"fat-cat\",\"fat-cats\",\"fatal\",\"fatalistic\",\"fatalistically\",\"fatally\",\"fatcat\",\"fatcats\",\"fateful\",\"fatefully\",\"fathomless\",\"fatigue\",\"fatigued\",\"fatique\",\"fatty\",\"fatuity\",\"fatuous\",\"fatuously\",\"fault\",\"faults\",\"faulty\",\"fawningly\",\"faze\",\"fear\",\"fearful\",\"fearfully\",\"fears\",\"fearsome\",\"feckless\",\"feeble\",\"feeblely\",\"feebleminded\",\"feign\",\"feint\",\"fell\",\"felon\",\"felonious\",\"ferociously\",\"ferocity\",\"fetid\",\"fever\",\"feverish\",\"fevers\",\"fiasco\",\"fib\",\"fibber\",\"fickle\",\"fiction\",\"fictional\",\"fictitious\",\"fidget\",\"fidgety\",\"fiend\",\"fiendish\",\"fierce\",\"figurehead\",\"filth\",\"filthy\",\"finagle\",\"finicky\",\"fissures\",\"fist\",\"flabbergast\",\"flabbergasted\",\"flagging\",\"flagrant\",\"flagrantly\",\"flair\",\"flairs\",\"flak\",\"flake\",\"flakey\",\"flakieness\",\"flaking\",\"flaky\",\"flare\",\"flares\",\"flareup\",\"flareups\",\"flat-out\",\"flaunt\",\"flaw\",\"flawed\",\"flaws\",\"flee\",\"fleed\",\"fleeing\",\"fleer\",\"flees\",\"fleeting\",\"flicering\",\"flicker\",\"flickering\",\"flickers\",\"flighty\",\"flimflam\",\"flimsy\",\"flirt\",\"flirty\",\"floored\",\"flounder\",\"floundering\",\"flout\",\"fluster\",\"foe\",\"fool\",\"fooled\",\"foolhardy\",\"foolish\",\"foolishly\",\"foolishness\",\"forbid\",\"forbidden\",\"forbidding\",\"forceful\",\"foreboding\",\"forebodingly\",\"forfeit\",\"forged\",\"forgetful\",\"forgetfully\",\"forgetfulness\",\"forlorn\",\"forlornly\",\"forsake\",\"forsaken\",\"forswear\",\"foul\",\"foully\",\"foulness\",\"fractious\",\"fractiously\",\"fracture\",\"fragile\",\"fragmented\",\"frail\",\"frantic\",\"frantically\",\"franticly\",\"fraud\",\"fraudulent\",\"fraught\",\"frazzle\",\"frazzled\",\"freak\",\"freaking\",\"freakish\",\"freakishly\",\"freaks\",\"freeze\",\"freezes\",\"freezing\",\"frenetic\",\"frenetically\",\"frenzied\",\"frenzy\",\"fret\",\"fretful\",\"frets\",\"friction\",\"frictions\",\"fried\",\"friggin\",\"frigging\",\"fright\",\"frighten\",\"frightening\",\"frighteningly\",\"frightful\",\"frightfully\",\"frigid\",\"frost\",\"frown\",\"froze\",\"frozen\",\"fruitless\",\"fruitlessly\",\"frustrate\",\"frustrated\",\"frustrates\",\"frustrating\",\"frustratingly\",\"frustration\",\"frustrations\",\"fuck\",\"fucking\",\"fudge\",\"fugitive\",\"full-blown\",\"fulminate\",\"fumble\",\"fume\",\"fumes\",\"fundamentalism\",\"funky\",\"funnily\",\"funny\",\"furious\",\"furiously\",\"furor\",\"fury\",\"fuss\",\"fussy\",\"fustigate\",\"fusty\",\"futile\",\"futilely\",\"futility\",\"fuzzy\",\"gabble\",\"gaff\",\"gaffe\",\"gainsay\",\"gainsayer\",\"gall\",\"galling\",\"gallingly\",\"galls\",\"gangster\",\"gape\",\"garbage\",\"garish\",\"gasp\",\"gauche\",\"gaudy\",\"gawk\",\"gawky\",\"geezer\",\"genocide\",\"get-rich\",\"ghastly\",\"ghetto\",\"ghosting\",\"gibber\",\"gibberish\",\"gibe\",\"giddy\",\"gimmick\",\"gimmicked\",\"gimmicking\",\"gimmicks\",\"gimmicky\",\"glare\",\"glaringly\",\"glib\",\"glibly\",\"glitch\",\"glitches\",\"gloatingly\",\"gloom\",\"gloomy\",\"glower\",\"glum\",\"glut\",\"gnawing\",\"goad\",\"goading\",\"god-awful\",\"goof\",\"goofy\",\"goon\",\"gossip\",\"graceless\",\"gracelessly\",\"graft\",\"grainy\",\"grapple\",\"grate\",\"grating\",\"gravely\",\"greasy\",\"greed\",\"greedy\",\"grief\",\"grievance\",\"grievances\",\"grieve\",\"grieving\",\"grievous\",\"grievously\",\"grim\",\"grimace\",\"grind\",\"gripe\",\"gripes\",\"grisly\",\"gritty\",\"gross\",\"grossly\",\"grotesque\",\"grouch\",\"grouchy\",\"groundless\",\"grouse\",\"growl\",\"grudge\",\"grudges\",\"grudging\",\"grudgingly\",\"gruesome\",\"gruesomely\",\"gruff\",\"grumble\",\"grumpier\",\"grumpiest\",\"grumpily\",\"grumpish\",\"grumpy\",\"guile\",\"guilt\",\"guiltily\",\"guilty\",\"gullible\",\"gutless\",\"gutter\",\"hack\",\"hacks\",\"haggard\",\"haggle\",\"hairloss\",\"halfhearted\",\"halfheartedly\",\"hallucinate\",\"hallucination\",\"hamper\",\"hampered\",\"handicapped\",\"hang\",\"hangs\",\"haphazard\",\"hapless\",\"harangue\",\"harass\",\"harassed\",\"harasses\",\"harassment\",\"harboring\",\"harbors\",\"hard\",\"hard-hit\",\"hard-line\",\"hard-liner\",\"hardball\",\"harden\",\"hardened\",\"hardheaded\",\"hardhearted\",\"hardliner\",\"hardliners\",\"hardship\",\"hardships\",\"harm\",\"harmed\",\"harmful\",\"harms\",\"harpy\",\"harridan\",\"harried\",\"harrow\",\"harsh\",\"harshly\",\"hasseling\",\"hassle\",\"hassled\",\"hassles\",\"haste\",\"hastily\",\"hasty\",\"hate\",\"hated\",\"hateful\",\"hatefully\",\"hatefulness\",\"hater\",\"haters\",\"hates\",\"hating\",\"hatred\",\"haughtily\",\"haughty\",\"haunt\",\"haunting\",\"havoc\",\"hawkish\",\"haywire\",\"hazard\",\"hazardous\",\"haze\",\"hazy\",\"head-aches\",\"headache\",\"headaches\",\"heartbreaker\",\"heartbreaking\",\"heartbreakingly\",\"heartless\",\"heathen\",\"heavy-handed\",\"heavyhearted\",\"heck\",\"heckle\",\"heckled\",\"heckles\",\"hectic\",\"hedge\",\"hedonistic\",\"heedless\",\"hefty\",\"hegemonism\",\"hegemonistic\",\"hegemony\",\"heinous\",\"hell\",\"hell-bent\",\"hellion\",\"hells\",\"helpless\",\"helplessly\",\"helplessness\",\"heresy\",\"heretic\",\"heretical\",\"hesitant\",\"hestitant\",\"hideous\",\"hideously\",\"hideousness\",\"high-priced\",\"hiliarious\",\"hinder\",\"hindrance\",\"hiss\",\"hissed\",\"hissing\",\"ho-hum\",\"hoard\",\"hoax\",\"hobble\",\"hogs\",\"hollow\",\"hoodium\",\"hoodwink\",\"hooligan\",\"hopeless\",\"hopelessly\",\"hopelessness\",\"horde\",\"horrendous\",\"horrendously\",\"horrible\",\"horrid\",\"horrific\",\"horrified\",\"horrifies\",\"horrify\",\"horrifying\",\"horrifys\",\"hostage\",\"hostile\",\"hostilities\",\"hostility\",\"hotbeds\",\"hothead\",\"hotheaded\",\"hothouse\",\"hubris\",\"huckster\",\"hum\",\"humid\",\"humiliate\",\"humiliating\",\"humiliation\",\"humming\",\"hung\",\"hurt\",\"hurted\",\"hurtful\",\"hurting\",\"hurts\",\"hustler\",\"hype\",\"hypocricy\",\"hypocrisy\",\"hypocrite\",\"hypocrites\",\"hypocritical\",\"hypocritically\",\"hysteria\",\"hysteric\",\"hysterical\",\"hysterically\",\"hysterics\",\"idiocies\",\"idiocy\",\"idiot\",\"idiotic\",\"idiotically\",\"idiots\",\"idle\",\"ignoble\",\"ignominious\",\"ignominiously\",\"ignominy\",\"ignorance\",\"ignorant\",\"ignore\",\"ill-advised\",\"ill-conceived\",\"ill-defined\",\"ill-designed\",\"ill-fated\",\"ill-favored\",\"ill-formed\",\"ill-mannered\",\"ill-natured\",\"ill-sorted\",\"ill-tempered\",\"ill-treated\",\"ill-treatment\",\"ill-usage\",\"ill-used\",\"illegal\",\"illegally\",\"illegitimate\",\"illicit\",\"illiterate\",\"illness\",\"illogic\",\"illogical\",\"illogically\",\"illusion\",\"illusions\",\"illusory\",\"imaginary\",\"imbalance\",\"imbecile\",\"imbroglio\",\"immaterial\",\"immature\",\"imminence\",\"imminently\",\"immobilized\",\"immoderate\",\"immoderately\",\"immodest\",\"immoral\",\"immorality\",\"immorally\",\"immovable\",\"impair\",\"impaired\",\"impasse\",\"impatience\",\"impatient\",\"impatiently\",\"impeach\",\"impedance\",\"impede\",\"impediment\",\"impending\",\"impenitent\",\"imperfect\",\"imperfection\",\"imperfections\",\"imperfectly\",\"imperialist\",\"imperil\",\"imperious\",\"imperiously\",\"impermissible\",\"impersonal\",\"impertinent\",\"impetuous\",\"impetuously\",\"impiety\",\"impinge\",\"impious\",\"implacable\",\"implausible\",\"implausibly\",\"implicate\",\"implication\",\"implode\",\"impolite\",\"impolitely\",\"impolitic\",\"importunate\",\"importune\",\"impose\",\"imposers\",\"imposing\",\"imposition\",\"impossible\",\"impossiblity\",\"impossibly\",\"impotent\",\"impoverish\",\"impoverished\",\"impractical\",\"imprecate\",\"imprecise\",\"imprecisely\",\"imprecision\",\"imprison\",\"imprisonment\",\"improbability\",\"improbable\",\"improbably\",\"improper\",\"improperly\",\"impropriety\",\"imprudence\",\"imprudent\",\"impudence\",\"impudent\",\"impudently\",\"impugn\",\"impulsive\",\"impulsively\",\"impunity\",\"impure\",\"impurity\",\"inability\",\"inaccuracies\",\"inaccuracy\",\"inaccurate\",\"inaccurately\",\"inaction\",\"inactive\",\"inadequacy\",\"inadequate\",\"inadequately\",\"inadverent\",\"inadverently\",\"inadvisable\",\"inadvisably\",\"inane\",\"inanely\",\"inappropriate\",\"inappropriately\",\"inapt\",\"inaptitude\",\"inarticulate\",\"inattentive\",\"inaudible\",\"incapable\",\"incapably\",\"incautious\",\"incendiary\",\"incense\"};\n" +
                            "public static final String[] POSITIVE_WORDS = {\"a+\",\"abound\",\"abounds\",\"abundance\",\"abundant\",\"accessable\",\"accessible\",\"acclaim\",\"acclaimed\",\"acclamation\",\"accolade\",\"accolades\",\"accommodative\",\"accomodative\",\"accomplish\",\"accomplished\",\"accomplishment\",\"accomplishments\",\"accurate\",\"accurately\",\"achievable\",\"achievement\",\"achievements\",\"achievible\",\"acumen\",\"adaptable\",\"adaptive\",\"adequate\",\"adjustable\",\"admirable\",\"admirably\",\"admiration\",\"admire\",\"admirer\",\"admiring\",\"admiringly\",\"adorable\",\"adore\",\"adored\",\"adorer\",\"adoring\",\"adoringly\",\"adroit\",\"adroitly\",\"adulate\",\"adulation\",\"adulatory\",\"advanced\",\"advantage\",\"advantageous\",\"advantageously\",\"advantages\",\"adventuresome\",\"adventurous\",\"advocate\",\"advocated\",\"advocates\",\"affability\",\"affable\",\"affably\",\"affectation\",\"affection\",\"affectionate\",\"affinity\",\"affirm\",\"affirmation\",\"affirmative\",\"affluence\",\"affluent\",\"afford\",\"affordable\",\"affordably\",\"afordable\",\"agile\",\"agilely\",\"agility\",\"agreeable\",\"agreeableness\",\"agreeably\",\"all-around\",\"alluring\",\"alluringly\",\"altruistic\",\"altruistically\",\"amaze\",\"amazed\",\"amazement\",\"amazes\",\"amazing\",\"amazingly\",\"ambitious\",\"ambitiously\",\"ameliorate\",\"amenable\",\"amenity\",\"amiability\",\"amiabily\",\"amiable\",\"amicability\",\"amicable\",\"amicably\",\"amity\",\"ample\",\"amply\",\"amuse\",\"amusing\",\"amusingly\",\"angel\",\"angelic\",\"apotheosis\",\"appeal\",\"appealing\",\"applaud\",\"appreciable\",\"appreciate\",\"appreciated\",\"appreciates\",\"appreciative\",\"appreciatively\",\"appropriate\",\"approval\",\"approve\",\"ardent\",\"ardently\",\"ardor\",\"articulate\",\"aspiration\",\"aspirations\",\"aspire\",\"assurance\",\"assurances\",\"assure\",\"assuredly\",\"assuring\",\"astonish\",\"astonished\",\"astonishing\",\"astonishingly\",\"astonishment\",\"astound\",\"astounded\",\"astounding\",\"astoundingly\",\"astutely\",\"attentive\",\"attraction\",\"attractive\",\"attractively\",\"attune\",\"audible\",\"audibly\",\"auspicious\",\"authentic\",\"authoritative\",\"autonomous\",\"available\",\"aver\",\"avid\",\"avidly\",\"award\",\"awarded\",\"awards\",\"awe\",\"awed\",\"awesome\",\"awesomely\",\"awesomeness\",\"awestruck\",\"awsome\",\"backbone\",\"balanced\",\"bargain\",\"beauteous\",\"beautiful\",\"beautifullly\",\"beautifully\",\"beautify\",\"beauty\",\"beckon\",\"beckoned\",\"beckoning\",\"beckons\",\"believable\",\"believeable\",\"beloved\",\"benefactor\",\"beneficent\",\"beneficial\",\"beneficially\",\"beneficiary\",\"benefit\",\"benefits\",\"benevolence\",\"benevolent\",\"benifits\",\"best\",\"best-known\",\"best-performing\",\"best-selling\",\"better\",\"better-known\",\"better-than-expected\",\"beutifully\",\"blameless\",\"bless\",\"blessing\",\"bliss\",\"blissful\",\"blissfully\",\"blithe\",\"blockbuster\",\"bloom\",\"blossom\",\"bolster\",\"bonny\",\"bonus\",\"bonuses\",\"boom\",\"booming\",\"boost\",\"boundless\",\"bountiful\",\"brainiest\",\"brainy\",\"brand-new\",\"brave\",\"bravery\",\"bravo\",\"breakthrough\",\"breakthroughs\",\"breathlessness\",\"breathtaking\",\"breathtakingly\",\"breeze\",\"bright\",\"brighten\",\"brighter\",\"brightest\",\"brilliance\",\"brilliances\",\"brilliant\",\"brilliantly\",\"brisk\",\"brotherly\",\"bullish\",\"buoyant\",\"cajole\",\"calm\",\"calming\",\"calmness\",\"capability\",\"capable\",\"capably\",\"captivate\",\"captivating\",\"carefree\",\"cashback\",\"cashbacks\",\"catchy\",\"celebrate\",\"celebrated\",\"celebration\",\"celebratory\",\"champ\",\"champion\",\"charisma\",\"charismatic\",\"charitable\",\"charm\",\"charming\",\"charmingly\",\"chaste\",\"cheaper\",\"cheapest\",\"cheer\",\"cheerful\",\"cheery\",\"cherish\",\"cherished\",\"cherub\",\"chic\",\"chivalrous\",\"chivalry\",\"civility\",\"civilize\",\"clarity\",\"classic\",\"classy\",\"clean\",\"cleaner\",\"cleanest\",\"cleanliness\",\"cleanly\",\"clear\",\"clear-cut\",\"cleared\",\"clearer\",\"clearly\",\"clears\",\"clever\",\"cleverly\",\"cohere\",\"coherence\",\"coherent\",\"cohesive\",\"colorful\",\"comely\",\"comfort\",\"comfortable\",\"comfortably\",\"comforting\",\"comfy\",\"commend\",\"commendable\",\"commendably\",\"commitment\",\"commodious\",\"compact\",\"compactly\",\"compassion\",\"compassionate\",\"compatible\",\"competitive\",\"complement\",\"complementary\",\"complemented\",\"complements\",\"compliant\",\"compliment\",\"complimentary\",\"comprehensive\",\"conciliate\",\"conciliatory\",\"concise\",\"confidence\",\"confident\",\"congenial\",\"congratulate\",\"congratulation\",\"congratulations\",\"congratulatory\",\"conscientious\",\"considerate\",\"consistent\",\"consistently\",\"constructive\",\"consummate\",\"contentment\",\"continuity\",\"contrasty\",\"contribution\",\"convenience\",\"convenient\",\"conveniently\",\"convience\",\"convienient\",\"convient\",\"convincing\",\"convincingly\",\"cool\",\"coolest\",\"cooperative\",\"cooperatively\",\"cornerstone\",\"correct\",\"correctly\",\"cost-effective\",\"cost-saving\",\"counter-attack\",\"counter-attacks\",\"courage\",\"courageous\",\"courageously\",\"courageousness\",\"courteous\",\"courtly\",\"covenant\",\"cozy\",\"creative\",\"credence\",\"credible\",\"crisp\",\"crisper\",\"cure\",\"cure-all\",\"cushy\",\"cute\",\"cuteness\",\"danke\",\"danken\",\"daring\",\"daringly\",\"darling\",\"dashing\",\"dauntless\",\"dawn\",\"dazzle\",\"dazzled\",\"dazzling\",\"dead-cheap\",\"dead-on\",\"decency\",\"decent\",\"decisive\",\"decisiveness\",\"dedicated\",\"defeat\",\"defeated\",\"defeating\",\"defeats\",\"defender\",\"deference\",\"deft\",\"deginified\",\"delectable\",\"delicacy\",\"delicate\",\"delicious\",\"delight\",\"delighted\",\"delightful\",\"delightfully\",\"delightfulness\",\"dependable\",\"dependably\",\"deservedly\",\"deserving\",\"desirable\",\"desiring\",\"desirous\",\"destiny\",\"detachable\",\"devout\",\"dexterous\",\"dexterously\",\"dextrous\",\"dignified\",\"dignify\",\"dignity\",\"diligence\",\"diligent\",\"diligently\",\"diplomatic\",\"dirt-cheap\",\"distinction\",\"distinctive\",\"distinguished\",\"diversified\",\"divine\",\"divinely\",\"dominate\",\"dominated\",\"dominates\",\"dote\",\"dotingly\",\"doubtless\",\"dreamland\",\"dumbfounded\",\"dumbfounding\",\"dummy-proof\",\"durable\",\"dynamic\",\"eager\",\"eagerly\",\"eagerness\",\"earnest\",\"earnestly\",\"earnestness\",\"ease\",\"eased\",\"eases\",\"easier\",\"easiest\",\"easiness\",\"easing\",\"easy\",\"easy-to-use\",\"easygoing\",\"ebullience\",\"ebullient\",\"ebulliently\",\"ecenomical\",\"economical\",\"ecstasies\",\"ecstasy\",\"ecstatic\",\"ecstatically\",\"edify\",\"educated\",\"effective\",\"effectively\",\"effectiveness\",\"effectual\",\"efficacious\",\"efficient\",\"efficiently\",\"effortless\",\"effortlessly\",\"effusion\",\"effusive\",\"effusively\",\"effusiveness\",\"elan\",\"elate\",\"elated\",\"elatedly\",\"elation\",\"electrify\",\"elegance\",\"elegant\",\"elegantly\",\"elevate\",\"elite\",\"eloquence\",\"eloquent\",\"eloquently\",\"embolden\",\"eminence\",\"eminent\",\"empathize\",\"empathy\",\"empower\",\"empowerment\",\"enchant\",\"enchanted\",\"enchanting\",\"enchantingly\",\"encourage\",\"encouragement\",\"encouraging\",\"encouragingly\",\"endear\",\"endearing\",\"endorse\",\"endorsed\",\"endorsement\",\"endorses\",\"endorsing\",\"energetic\",\"energize\",\"energy-efficient\",\"energy-saving\",\"engaging\",\"engrossing\",\"enhance\",\"enhanced\",\"enhancement\",\"enhances\",\"enjoy\",\"enjoyable\",\"enjoyably\",\"enjoyed\",\"enjoying\",\"enjoyment\",\"enjoys\",\"enlighten\",\"enlightenment\",\"enliven\",\"ennoble\",\"enough\",\"enrapt\",\"enrapture\",\"enraptured\",\"enrich\",\"enrichment\",\"enterprising\",\"entertain\",\"entertaining\",\"entertains\",\"enthral\",\"enthrall\",\"enthralled\",\"enthuse\",\"enthusiasm\",\"enthusiast\",\"enthusiastic\",\"enthusiastically\",\"entice\",\"enticed\",\"enticing\",\"enticingly\",\"entranced\",\"entrancing\",\"entrust\",\"enviable\",\"enviably\",\"envious\",\"enviously\",\"enviousness\",\"envy\",\"equitable\",\"ergonomical\",\"err-free\",\"erudite\",\"ethical\",\"eulogize\",\"euphoria\",\"euphoric\",\"euphorically\",\"evaluative\",\"evenly\",\"eventful\",\"everlasting\",\"evocative\",\"exalt\",\"exaltation\",\"exalted\",\"exaltedly\",\"exalting\",\"exaltingly\",\"examplar\",\"examplary\",\"excallent\",\"exceed\",\"exceeded\",\"exceeding\",\"exceedingly\",\"exceeds\",\"excel\",\"exceled\",\"excelent\",\"excellant\",\"excelled\",\"excellence\",\"excellency\",\"excellent\",\"excellently\",\"excels\",\"exceptional\",\"exceptionally\",\"excite\",\"excited\",\"excitedly\",\"excitedness\",\"excitement\",\"excites\",\"exciting\",\"excitingly\",\"exellent\",\"exemplar\",\"exemplary\",\"exhilarate\",\"exhilarating\",\"exhilaratingly\",\"exhilaration\",\"exonerate\",\"expansive\",\"expeditiously\",\"expertly\",\"exquisite\",\"exquisitely\",\"extol\",\"extoll\",\"extraordinarily\",\"extraordinary\",\"exuberance\",\"exuberant\",\"exuberantly\",\"exult\",\"exultant\",\"exultation\",\"exultingly\",\"eye-catch\",\"eye-catching\",\"eyecatch\",\"eyecatching\",\"fabulous\",\"fabulously\",\"facilitate\",\"fair\",\"fairly\",\"fairness\",\"faith\",\"faithful\",\"faithfully\",\"faithfulness\",\"fame\",\"famed\",\"famous\",\"famously\",\"fancier\",\"fancinating\",\"fancy\",\"fanfare\",\"fans\",\"fantastic\",\"fantastically\",\"fascinate\",\"fascinating\",\"fascinatingly\",\"fascination\",\"fashionable\",\"fashionably\",\"fast\",\"fast-growing\",\"fast-paced\",\"faster\",\"fastest\",\"fastest-growing\",\"faultless\",\"fav\",\"fave\",\"favor\",\"favorable\",\"favored\",\"favorite\",\"favorited\",\"favour\",\"fearless\",\"fearlessly\",\"feasible\",\"feasibly\",\"feat\",\"feature-rich\",\"fecilitous\",\"feisty\",\"felicitate\",\"felicitous\",\"felicity\",\"fertile\",\"fervent\",\"fervently\",\"fervid\",\"fervidly\",\"fervor\",\"festive\",\"fidelity\",\"fiery\",\"fine\",\"fine-looking\",\"finely\",\"finer\",\"finest\",\"firmer\",\"first-class\",\"first-in-class\",\"first-rate\",\"flashy\",\"flatter\",\"flattering\",\"flatteringly\",\"flawless\",\"flawlessly\",\"flexibility\",\"flexible\",\"flourish\",\"flourishing\",\"fluent\",\"flutter\",\"fond\",\"fondly\",\"fondness\",\"foolproof\",\"foremost\",\"foresight\",\"formidable\",\"fortitude\",\"fortuitous\",\"fortuitously\",\"fortunate\",\"fortunately\",\"fortune\",\"fragrant\",\"free\",\"freed\",\"freedom\",\"freedoms\",\"fresh\",\"fresher\",\"freshest\",\"friendliness\",\"friendly\",\"frolic\",\"frugal\",\"fruitful\",\"ftw\",\"fulfillment\",\"fun\",\"futurestic\",\"futuristic\",\"gaiety\",\"gaily\",\"gain\",\"gained\",\"gainful\",\"gainfully\",\"gaining\",\"gains\",\"gallant\",\"gallantly\",\"galore\",\"geekier\",\"geeky\",\"gem\",\"gems\",\"generosity\",\"generous\",\"generously\",\"genial\",\"genius\",\"gentle\",\"gentlest\",\"genuine\",\"gifted\",\"glad\",\"gladden\",\"gladly\",\"gladness\",\"glamorous\",\"glee\",\"gleeful\",\"gleefully\",\"glimmer\",\"glimmering\",\"glisten\",\"glistening\",\"glitter\",\"glitz\",\"glorify\",\"glorious\",\"gloriously\",\"glory\",\"glow\",\"glowing\",\"glowingly\",\"god-given\",\"god-send\",\"godlike\",\"godsend\",\"gold\",\"golden\",\"good\",\"goodly\",\"goodness\",\"goodwill\",\"goood\",\"gooood\",\"gorgeous\",\"gorgeously\",\"grace\",\"graceful\",\"gracefully\",\"gracious\",\"graciously\",\"graciousness\",\"grand\",\"grandeur\",\"grateful\",\"gratefully\",\"gratification\",\"gratified\",\"gratifies\",\"gratify\",\"gratifying\",\"gratifyingly\",\"gratitude\",\"great\",\"greatest\",\"greatness\",\"grin\",\"groundbreaking\",\"guarantee\",\"guidance\",\"guiltless\",\"gumption\",\"gush\",\"gusto\",\"gutsy\",\"hail\",\"halcyon\",\"hale\",\"hallmark\",\"hallmarks\",\"hallowed\",\"handier\",\"handily\",\"hands-down\",\"handsome\",\"handsomely\",\"handy\",\"happier\",\"happily\",\"happiness\",\"happy\",\"hard-working\",\"hardier\",\"hardy\",\"harmless\",\"harmonious\",\"harmoniously\",\"harmonize\",\"harmony\",\"headway\",\"heal\",\"healthful\",\"healthy\",\"hearten\",\"heartening\",\"heartfelt\",\"heartily\",\"heartwarming\",\"heaven\",\"heavenly\",\"helped\",\"helpful\",\"helping\",\"hero\",\"heroic\",\"heroically\",\"heroine\",\"heroize\",\"heros\",\"high-quality\",\"high-spirited\",\"hilarious\",\"holy\",\"homage\",\"honest\",\"honesty\",\"honor\",\"honorable\",\"honored\",\"honoring\",\"hooray\",\"hopeful\",\"hospitable\",\"hot\",\"hotcake\",\"hotcakes\",\"hottest\",\"hug\",\"humane\",\"humble\",\"humility\",\"humor\",\"humorous\",\"humorously\",\"humour\",\"humourous\",\"ideal\",\"idealize\",\"ideally\",\"idol\",\"idolize\",\"idolized\",\"idyllic\",\"illuminate\",\"illuminati\",\"illuminating\",\"illumine\",\"illustrious\",\"ilu\",\"imaculate\",\"imaginative\",\"immaculate\",\"immaculately\",\"immense\",\"impartial\",\"impartiality\",\"impartially\",\"impassioned\",\"impeccable\",\"impeccably\",\"important\",\"impress\",\"impressed\",\"impresses\",\"impressive\",\"impressively\",\"impressiveness\",\"improve\",\"improved\",\"improvement\",\"improvements\",\"improves\",\"improving\",\"incredible\",\"incredibly\",\"indebted\",\"individualized\",\"indulgence\",\"indulgent\",\"industrious\",\"inestimable\",\"inestimably\",\"inexpensive\",\"infallibility\",\"infallible\",\"infallibly\",\"influential\",\"ingenious\",\"ingeniously\",\"ingenuity\",\"ingenuous\",\"ingenuously\",\"innocuous\",\"innovation\",\"innovative\",\"inpressed\",\"insightful\",\"insightfully\",\"inspiration\",\"inspirational\",\"inspire\",\"inspiring\",\"instantly\",\"instructive\",\"instrumental\",\"integral\",\"integrated\",\"intelligence\",\"intelligent\",\"intelligible\",\"interesting\",\"interests\",\"intimacy\",\"intimate\",\"intricate\",\"intrigue\",\"intriguing\",\"intriguingly\",\"intuitive\",\"invaluable\",\"invaluablely\",\"inventive\",\"invigorate\",\"invigorating\",\"invincibility\",\"invincible\",\"inviolable\",\"inviolate\",\"invulnerable\",\"irreplaceable\",\"irreproachable\",\"irresistible\",\"irresistibly\",\"issue-free\",\"jaw-droping\",\"jaw-dropping\",\"jollify\",\"jolly\",\"jovial\",\"joy\",\"joyful\",\"joyfully\",\"joyous\",\"joyously\",\"jubilant\",\"jubilantly\",\"jubilate\",\"jubilation\",\"jubiliant\",\"judicious\",\"justly\",\"keen\",\"keenly\",\"keenness\",\"kid-friendly\",\"kindliness\",\"kindly\",\"kindness\",\"knowledgeable\",\"kudos\",\"large-capacity\",\"laud\",\"laudable\",\"laudably\",\"lavish\",\"lavishly\",\"law-abiding\",\"lawful\",\"lawfully\",\"lead\",\"leading\",\"leads\",\"lean\",\"led\",\"legendary\",\"leverage\",\"levity\",\"liberate\",\"liberation\",\"liberty\",\"lifesaver\",\"light-hearted\",\"lighter\",\"likable\",\"like\",\"liked\",\"likes\",\"liking\",\"lionhearted\",\"lively\",\"logical\",\"long-lasting\",\"lovable\",\"lovably\",\"love\",\"loved\",\"loveliness\",\"lovely\",\"lover\",\"loves\",\"loving\",\"low-cost\",\"low-price\",\"low-priced\",\"low-risk\",\"lower-priced\",\"loyal\",\"loyalty\",\"lucid\",\"lucidly\",\"luck\",\"luckier\",\"luckiest\",\"luckiness\",\"lucky\",\"lucrative\",\"luminous\",\"lush\",\"luster\",\"lustrous\",\"luxuriant\",\"luxuriate\",\"luxurious\",\"luxuriously\",\"luxury\",\"lyrical\",\"magic\",\"magical\",\"magnanimous\",\"magnanimously\",\"magnificence\",\"magnificent\",\"magnificently\",\"majestic\",\"majesty\",\"manageable\",\"maneuverable\",\"marvel\",\"marveled\",\"marvelled\",\"marvellous\",\"marvelous\",\"marvelously\",\"marvelousness\",\"marvels\",\"master\",\"masterful\",\"masterfully\",\"masterpiece\",\"masterpieces\",\"masters\",\"mastery\",\"matchless\",\"mature\",\"maturely\",\"maturity\",\"meaningful\",\"memorable\",\"merciful\",\"mercifully\",\"mercy\",\"merit\",\"meritorious\",\"merrily\",\"merriment\",\"merriness\",\"merry\",\"mesmerize\",\"mesmerized\",\"mesmerizes\",\"mesmerizing\",\"mesmerizingly\",\"meticulous\",\"meticulously\",\"mightily\",\"mighty\",\"mind-blowing\",\"miracle\",\"miracles\",\"miraculous\",\"miraculously\",\"miraculousness\",\"modern\",\"modest\",\"modesty\",\"momentous\",\"monumental\",\"monumentally\",\"morality\",\"motivated\",\"multi-purpose\",\"navigable\",\"neat\",\"neatest\",\"neatly\",\"nice\",\"nicely\",\"nicer\",\"nicest\",\"nifty\",\"nimble\",\"noble\",\"nobly\",\"noiseless\",\"non-violence\",\"non-violent\",\"notably\",\"noteworthy\",\"nourish\",\"nourishing\",\"nourishment\",\"novelty\",\"nurturing\",\"oasis\",\"obsession\",\"obsessions\",\"obtainable\",\"openly\",\"openness\",\"optimal\",\"optimism\",\"optimistic\",\"opulent\",\"orderly\",\"originality\",\"outdo\",\"outdone\",\"outperform\",\"outperformed\",\"outperforming\",\"outperforms\",\"outshine\",\"outshone\",\"outsmart\",\"outstanding\",\"outstandingly\",\"outstrip\",\"outwit\",\"ovation\",\"overjoyed\",\"overtake\",\"overtaken\",\"overtakes\",\"overtaking\",\"overtook\",\"overture\",\"pain-free\",\"painless\",\"painlessly\",\"palatial\",\"pamper\",\"pampered\",\"pamperedly\",\"pamperedness\",\"pampers\",\"panoramic\",\"paradise\",\"paramount\",\"pardon\",\"passion\",\"passionate\",\"passionately\",\"patience\",\"patient\",\"patiently\",\"patriot\",\"patriotic\",\"peace\",\"peaceable\",\"peaceful\",\"peacefully\",\"peacekeepers\",\"peach\",\"peerless\",\"pep\",\"pepped\",\"pepping\",\"peppy\",\"peps\",\"perfect\",\"perfection\",\"perfectly\",\"permissible\",\"perseverance\",\"persevere\",\"personages\",\"personalized\",\"phenomenal\",\"phenomenally\",\"picturesque\",\"piety\",\"pinnacle\",\"playful\",\"playfully\",\"pleasant\",\"pleasantly\",\"pleased\",\"pleases\",\"pleasing\",\"pleasingly\",\"pleasurable\",\"pleasurably\",\"pleasure\",\"plentiful\",\"pluses\",\"plush\",\"plusses\",\"poetic\",\"poeticize\",\"poignant\",\"poise\",\"poised\",\"polished\",\"polite\",\"politeness\",\"popular\",\"portable\",\"posh\",\"positive\",\"positively\",\"positives\",\"powerful\",\"powerfully\",\"praise\",\"praiseworthy\",\"praising\",\"pre-eminent\",\"precious\",\"precise\",\"precisely\",\"preeminent\",\"prefer\",\"preferable\",\"preferably\",\"prefered\",\"preferes\",\"preferring\",\"prefers\",\"premier\",\"prestige\",\"prestigious\",\"prettily\",\"pretty\",\"priceless\",\"pride\",\"principled\",\"privilege\",\"privileged\",\"prize\",\"proactive\",\"problem-free\",\"problem-solver\",\"prodigious\",\"prodigiously\",\"prodigy\",\"productive\",\"productively\",\"proficient\",\"proficiently\",\"profound\",\"profoundly\",\"profuse\",\"profusion\",\"progress\",\"progressive\",\"prolific\",\"prominence\",\"prominent\",\"promise\",\"promised\",\"promises\",\"promising\",\"promoter\",\"prompt\",\"promptly\",\"proper\",\"properly\",\"propitious\",\"propitiously\",\"pros\",\"prosper\",\"prosperity\",\"prosperous\",\"prospros\",\"protect\",\"protection\",\"protective\",\"proud\",\"proven\",\"proves\",\"providence\",\"proving\",\"prowess\",\"prudence\",\"prudent\",\"prudently\",\"punctual\",\"pure\",\"purify\",\"purposeful\",\"quaint\",\"qualified\",\"qualify\",\"quicker\",\"quiet\",\"quieter\",\"radiance\",\"radiant\",\"rapid\",\"rapport\",\"rapt\",\"rapture\",\"raptureous\",\"raptureously\",\"rapturous\",\"rapturously\",\"rational\",\"razor-sharp\",\"reachable\",\"readable\",\"readily\",\"ready\",\"reaffirm\",\"reaffirmation\",\"realistic\",\"realizable\",\"reasonable\",\"reasonably\",\"reasoned\",\"reassurance\",\"reassure\",\"receptive\",\"reclaim\",\"recomend\",\"recommend\",\"recommendation\",\"recommendations\",\"recommended\",\"reconcile\",\"reconciliation\",\"record-setting\",\"recover\",\"recovery\",\"rectification\",\"rectify\",\"rectifying\",\"redeem\",\"redeeming\",\"redemption\",\"refine\",\"refined\",\"refinement\",\"reform\",\"reformed\",\"reforming\",\"reforms\",\"refresh\",\"refreshed\",\"refreshing\",\"refund\",\"refunded\",\"regal\",\"regally\",\"regard\",\"rejoice\",\"rejoicing\",\"rejoicingly\",\"rejuvenate\",\"rejuvenated\",\"rejuvenating\",\"relaxed\",\"relent\",\"reliable\",\"reliably\",\"relief\",\"relish\",\"remarkable\",\"remarkably\",\"remedy\",\"remission\",\"remunerate\",\"renaissance\",\"renewed\",\"renown\",\"renowned\",\"replaceable\",\"reputable\",\"reputation\",\"resilient\",\"resolute\",\"resound\",\"resounding\",\"resourceful\",\"resourcefulness\",\"respect\",\"respectable\",\"respectful\",\"respectfully\",\"respite\",\"resplendent\",\"responsibly\",\"responsive\",\"restful\",\"restored\",\"restructure\",\"restructured\",\"restructuring\",\"retractable\",\"revel\",\"revelation\",\"revere\",\"reverence\",\"reverent\",\"reverently\",\"revitalize\",\"revival\",\"revive\",\"revives\",\"revolutionary\",\"revolutionize\",\"revolutionized\",\"revolutionizes\",\"reward\",\"rewarding\",\"rewardingly\",\"rich\",\"richer\",\"richly\",\"richness\",\"right\",\"righten\",\"righteous\",\"righteously\",\"righteousness\",\"rightful\",\"rightfully\",\"rightly\",\"rightness\",\"risk-free\",\"robust\",\"rock-star\",\"rock-stars\",\"rockstar\",\"rockstars\",\"romantic\",\"romantically\",\"romanticize\",\"roomier\",\"roomy\",\"rosy\",\"safe\",\"safely\",\"sagacity\",\"sagely\",\"saint\",\"saintliness\",\"saintly\",\"salutary\",\"salute\",\"sane\",\"satisfactorily\",\"satisfactory\",\"satisfied\",\"satisfies\",\"satisfy\",\"satisfying\",\"satisified\",\"saver\",\"savings\",\"savior\",\"savvy\",\"scenic\",\"seamless\",\"seasoned\",\"secure\",\"securely\",\"selective\",\"self-determination\",\"self-respect\",\"self-satisfaction\",\"self-sufficiency\",\"self-sufficient\",\"sensation\",\"sensational\",\"sensationally\",\"sensations\",\"sensible\",\"sensibly\",\"sensitive\",\"serene\",\"serenity\",\"sexy\",\"sharp\",\"sharper\",\"sharpest\",\"shimmering\",\"shimmeringly\",\"shine\",\"shiny\",\"significant\",\"silent\",\"simpler\",\"simplest\",\"simplified\",\"simplifies\",\"simplify\",\"simplifying\",\"sincere\",\"sincerely\",\"sincerity\",\"skill\",\"skilled\",\"skillful\",\"skillfully\",\"slammin\",\"sleek\",\"slick\",\"smart\",\"smarter\",\"smartest\",\"smartly\",\"smile\",\"smiles\",\"smiling\",\"smilingly\",\"smitten\",\"smooth\",\"smoother\",\"smoothes\",\"smoothest\",\"smoothly\",\"snappy\",\"snazzy\",\"sociable\",\"soft\",\"softer\",\"solace\",\"solicitous\",\"solicitously\",\"solid\",\"solidarity\",\"soothe\",\"soothingly\",\"sophisticated\",\"soulful\",\"soundly\",\"soundness\",\"spacious\",\"sparkle\",\"sparkling\",\"spectacular\",\"spectacularly\",\"speedily\",\"speedy\",\"spellbind\",\"spellbinding\",\"spellbindingly\",\"spellbound\",\"spirited\",\"spiritual\",\"splendid\",\"splendidly\",\"splendor\",\"spontaneous\",\"sporty\",\"spotless\",\"sprightly\",\"stability\",\"stabilize\",\"stable\",\"stainless\",\"standout\",\"state-of-the-art\",\"stately\",\"statuesque\",\"staunch\",\"staunchly\",\"staunchness\",\"steadfast\",\"steadfastly\",\"steadfastness\",\"steadiest\",\"steadiness\",\"steady\",\"stellar\",\"stellarly\",\"stimulate\",\"stimulates\",\"stimulating\",\"stimulative\",\"stirringly\",\"straighten\",\"straightforward\",\"streamlined\",\"striking\",\"strikingly\",\"striving\",\"strong\",\"stronger\",\"strongest\",\"stunned\",\"stunning\",\"stunningly\",\"stupendous\",\"stupendously\",\"sturdier\",\"sturdy\",\"stylish\",\"stylishly\",\"stylized\",\"suave\",\"suavely\",\"sublime\",\"subsidize\",\"subsidized\",\"subsidizes\",\"subsidizing\",\"substantive\",\"succeed\",\"succeeded\",\"succeeding\",\"succeeds\",\"succes\",\"success\",\"successes\",\"successful\",\"successfully\",\"suffice\",\"sufficed\",\"suffices\",\"sufficient\",\"sufficiently\",\"suitable\",\"sumptuous\",\"sumptuously\",\"sumptuousness\",\"super\",\"superb\",\"superbly\",\"superior\",\"superiority\",\"supple\",\"support\",\"supported\",\"supporter\",\"supporting\",\"supportive\",\"supports\",\"supremacy\",\"supreme\",\"supremely\",\"supurb\",\"supurbly\",\"surmount\",\"surpass\",\"surreal\",\"survival\",\"survivor\",\"sustainability\",\"sustainable\",\"swank\",\"swankier\",\"swankiest\",\"swanky\",\"sweeping\",\"sweet\",\"sweeten\",\"sweetheart\",\"sweetly\",\"sweetness\",\"swift\",\"swiftness\",\"talent\",\"talented\",\"talents\",\"tantalize\",\"tantalizing\",\"tantalizingly\",\"tempt\",\"tempting\",\"temptingly\",\"tenacious\",\"tenaciously\",\"tenacity\",\"tender\",\"tenderly\",\"terrific\",\"terrifically\",\"thank\",\"thankful\",\"thinner\",\"thoughtful\",\"thoughtfully\",\"thoughtfulness\",\"thrift\",\"thrifty\",\"thrill\",\"thrilled\",\"thrilling\",\"thrillingly\",\"thrills\",\"thrive\",\"thriving\",\"thumb-up\",\"thumbs-up\",\"tickle\",\"tidy\",\"time-honored\",\"timely\",\"tingle\",\"titillate\",\"titillating\",\"titillatingly\",\"togetherness\",\"tolerable\",\"toll-free\",\"top\",\"top-notch\",\"top-quality\",\"topnotch\",\"tops\",\"tough\",\"tougher\",\"toughest\",\"traction\",\"tranquil\",\"tranquility\",\"transparent\",\"treasure\",\"tremendously\",\"trendy\",\"triumph\",\"triumphal\",\"triumphant\",\"triumphantly\",\"trivially\",\"trophy\",\"trouble-free\",\"trump\",\"trumpet\",\"trust\",\"trusted\",\"trusting\",\"trustingly\",\"trustworthiness\",\"trustworthy\",\"trusty\",\"truthful\",\"truthfully\",\"truthfulness\",\"twinkly\",\"ultra-crisp\",\"unabashed\",\"unabashedly\",\"unaffected\",\"unassailable\",\"unbeatable\",\"unbiased\",\"unbound\",\"uncomplicated\",\"unconditional\",\"undamaged\",\"undaunted\",\"understandable\",\"undisputable\",\"undisputably\",\"undisputed\",\"unencumbered\",\"unequivocal\",\"unequivocally\",\"unfazed\",\"unfettered\",\"unforgettable\",\"unity\",\"unlimited\",\"unmatched\",\"unparalleled\",\"unquestionable\",\"unquestionably\",\"unreal\",\"unrestricted\",\"unrivaled\",\"unselfish\",\"unwavering\",\"upbeat\",\"upgradable\",\"upgradeable\",\"upgraded\",\"upheld\",\"uphold\",\"uplift\",\"uplifting\",\"upliftingly\",\"upliftment\",\"upscale\",\"usable\",\"useable\",\"useful\",\"user-friendly\",\"user-replaceable\",\"valiant\",\"valiantly\",\"valor\",\"valuable\",\"variety\",\"venerate\",\"verifiable\",\"veritable\",\"versatile\",\"versatility\",\"vibrant\",\"vibrantly\",\"victorious\",\"victory\",\"viewable\",\"vigilance\",\"vigilant\",\"virtue\",\"virtuous\",\"virtuously\",\"visionary\",\"vivacious\",\"vivid\",\"vouch\",\"vouchsafe\",\"warm\",\"warmer\",\"warmhearted\",\"warmly\",\"warmth\",\"wealthy\",\"welcome\",\"well\",\"well-backlit\",\"well-balanced\",\"well-behaved\",\"well-being\",\"well-bred\",\"well-connected\",\"well-educated\",\"well-established\",\"well-informed\",\"well-intentioned\",\"well-known\",\"well-made\",\"well-managed\",\"well-mannered\",\"well-positioned\",\"well-received\",\"well-regarded\",\"well-rounded\",\"well-run\",\"well-wishers\",\"wellbeing\",\"whoa\",\"wholeheartedly\",\"wholesome\",\"whooa\",\"whoooa\",\"wieldy\",\"willing\",\"willingly\",\"willingness\",\"win\",\"windfall\",\"winnable\",\"winner\",\"winners\",\"winning\",\"wins\",\"wisdom\",\"wise\",\"wisely\",\"witty\",\"won\",\"wonder\",\"wonderful\",\"wonderfully\",\"wonderous\",\"wonderously\",\"wonders\",\"wondrous\",\"woo\",\"work\",\"workable\",\"worked\",\"works\",\"world-famous\",\"worth\",\"worth-while\",\"worthiness\",\"worthwhile\",\"worthy\",\"wow\",\"wowed\",\"wowing\",\"wows\",\"yay\",\"youthful\",\"zeal\",\"zenith\",\"zest\",\"zippy\"};\n" +
                            "\n" +
                            "public static String getPerception (String pageContent){\n" +
                            "    String[] pageSentences={};\n" +
                            "\n" +
                            "    String filteredSentences=\"\";\n" +
                            "\n" +
                            "    filteredSentences = filterSentences(pageContent);\n" +
                            "\n" +
                            "    int positiveWords = 0;\n" +
                            "    int negativeWords = 0;\n" +
                            "\n" +
                            "    negativeWords = countNegativePerceptionWords(filteredSentences);\n" +
                            "    positiveWords = countPositivePerceptionWords(filteredSentences);\n" +
                            "\n" +
                            "    String sentiment = getSentiment(positiveWords, negativeWords);\n" +
                            "\n" +
                            "    System.out.println(\"filteredSentences: \"+filteredSentences);\n" +
                            "    System.out.println (\"positiveWords: \"+positiveWords);\n" +
                            "    System.out.println (\"negativeWords: \"+negativeWords);\n" +
                            "\n" +
                            "    return sentiment;\n" +
                            "}\n" +
                            "\n" +
                            "public static String getSentiment (int positiveWords, int negativeWords){\n" +
                            "    String ret = \"\";\n" +
                            "\n" +
                            "    if(positiveWords == negativeWords){\n" +
                            "        ret = \"NEUTRAL\";\n" +
                            "    }else if(positiveWords > negativeWords){\n" +
                            "        ret = \"GOOD\";\n" +
                            "    }else{\n" +
                            "        ret = \"BAD\";\n" +
                            "    }\n" +
                            "\n" +
                            "    return ret;\n" +
                            "}\n" +
                            "\n" +
                            "public static int countPositivePerceptionWords(String filteredSentences)\n" +
                            "{\n" +
                            "    int positiveCount=0;\n" +
                            "    String[] wordsInTheSentence = filteredSentences.split(\" \");\n" +
                            "    for(int x = 0; x < POSITIVE_WORDS.length; x++)\n" +
                            "    {                    \n" +
                            "        for(int y = 0; y < wordsInTheSentence.length; y++)\n" +
                            "        {\n" +
                            "            if(wordsInTheSentence[y].equalsIgnoreCase(POSITIVE_WORDS[x]))\n" +
                            "            {\n" +
                            "                positiveCount++;\n" +
                            "            }\n" +
                            "        }\n" +
                            "    }\n" +
                            "    return positiveCount;\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "public static int countNegativePerceptionWords(String filteredSentences){\n" +
                            "    int negativeCount=0;\n" +
                            "    String[] wordsInTheSentence = filteredSentences.split(\" \");\n" +
                            "    for(int x = 0; x < NEGATIVE_WORDS.length; x++)\n" +
                            "    {                    \n" +
                            "        for(int y = 0; y < wordsInTheSentence.length; y++)\n" +
                            "        {\n" +
                            "            if(wordsInTheSentence[y].equalsIgnoreCase(NEGATIVE_WORDS[x]))\n" +
                            "            {\n" +
                            "                negativeCount++;\n" +
                            "            }\n" +
                            "        }\n" +
                            "    }\n" +
                            "    return negativeCount;\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "public static String filterSentences(String pageContent){\n" +
                            "    //pageContent = \"Thats.is.it.\";\n" +
                            "    //pageContent = pageContent.replaceAll(\"\\\\.\", \" \");\n" +
                            "    //System.out.println(\"1.0 This is it!\");\n" +
                            "    String filteredSentences = \"\";\n" +
                            "    String[] sentences = pageContent.split(\"\\\\.\");\n" +
                            "    int l=(sentences.length);\n" +
                            "    try{\n" +
                            "        for(int x = 0 ; x<l; x++)\n" +
                            "        {\n" +
                            "\n" +
                            "            if(true)\n" +
                            "            {   \n" +
                            "                filteredSentences+=\" \"+sentences[x];\n" +
                            "            }\n" +
                            "\n" +
                            "        }\n" +
                            "    }catch(ArrayIndexOutOfBoundsException ex){\n" +
                            "\n" +
                            "    }\n" +
                            "\n" +
                            "    return filteredSentences;\n" +
                            "}";
        
        String classClose = "\n\n}//End of Main class";
//        System.out.println("thisJavaCode " + javaCode);
//        javaCode = pkg + head + javaCode + mainClose + builtInFunctions+ classClose;

        javaCode = pkg + head + javaCode + mainClose + classClose;
//        builtInFunctions = builtInFunctions.replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n");
        javaCode = javaCode.replace("volunteerCode = \"\"", "volunteerCode = \""+builtInFunctions+"\"");

//        javaCode = pkg + head + javaCode + close;
        
//        System.out.println(javaCode);
        javaConverted = javaCode;
//        PrintWriter out = new PrintWriter("src/java/interpreter_output/Main.java");
//        out.println(javaCode);
//        out.close();
    }
    
    
  private static String readTxtFileLines (String path){
        String ret = "";
        
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                ret += sCurrentLine+"\n";
            }   

        } catch (IOException e) {
            System.out.println(e.toString());
        }
        
        return ret;
    }
    
    private static boolean isArray(String stmt){
        stmt = stmt.trim();
        if(stmt.length()>0 && stmt.charAt(0)=='['){
            stmt = stmt.replace(" ", "");
            if(stmt.charAt((stmt.length()-1))==']'){
                return true;
            }
        }
        return false;
    }
    
    private static String convertArray(String stmt){
        stmt = stmt.replace(" ", "");
        stmt = stmt.replace("[", "");
        stmt = stmt.replace("]", "");
        String[] elements = stmt.split(",");
        
        //Get the array type
        String arrType = "new ";
        if(elements.length > 0){
            try{
                Double.parseDouble(elements[0]);
                arrType += "double[]{";
            }catch(Exception e){
                arrType += "String[]{";
            }
        }
        
        for(String elem: elements){
            arrType += elem+",";
        }
        
        arrType += "};";
        arrType = arrType.replace(",};", "};");
        return arrType;
    }
    
    /**
     * Author: Heinrich
     * @param stmt
     * @return 
     */
    private static String convertDeclarationStmt(String stmt){
        List<String> words = Arrays.asList(stmt.split(" +"));
        String variableName = words.get(1);
        String dataType = words.get(3);
        if(dataType.equals("number")){
            dataType = "double";
        }else if(dataType.equals("string")){
            dataType = "String";
        }else if(dataType.equals("numberlist")){
            dataType = "double[]";
        }else if(dataType.equals("stringlist")){
            dataType = "String[]";
        }
        
        hashMap.put(variableName, dataType);
        
        return dataType+" "+variableName+";";
    }
    
    private static boolean isDeclarationStmt(String stmt){
        List<String> words = Arrays.asList(stmt.split(" "));
        if(words.get(0).equals("define")){
            return true;
        }
        return false;
    }
    
    
    private static String readTxtFile(String path){
        String ret = "";
        
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                ret += sCurrentLine;
            }   

        } catch (IOException e) {
            System.out.println(e.toString());
        }
        
        return ret;
    }
    
    
    public static boolean isCondition(String stmt){
        List<String> words = Arrays.asList(stmt.split(" +"));
        if(words.get(0).equals("if") || words.get(0).equals("else") || words.get(0).equals("elseif")){
            return true;
        }
        return false;
    }
    
    public static String convertConditionStmt(String stmt){
        stmt = stmt.replace(";", "");
        List<String> words = Arrays.asList(stmt.split(" +"));
        if(words.get(0).equals("elseif")){
            words.set(0, "else if");
            stmt = "";
            for(String word: words){
                stmt += word+" ";
            }
        }
        
        return stmt;       
    }
    
    
    public static boolean isFunctionDefinition(String stmt){
        List<String> words = Arrays.asList(stmt.split(" +"));
        if(words.get(0).equals("function")){
            return true;
        }
        return false;
    }
    
    /**
     * TODOD: double check how a function parameter is declared.
     * @param stmt
     * @return 
     */
    public static String convertFunctionDefinitionStmt(String stmt){
        List<String> words = Arrays.asList(stmt.split(" +"));
        int flag = 0;
        String ret;
        for(String word:words){
            if(word.equals("main")){
                flag=1;
                break;
            }
        }
        if(flag == 1 ){
            ret = "public static String start(){";
              //public static String main ()
        } else {
            stmt = stmt.replace(";", "");
            stmt = stmt.replace("function", "public static");
            stmt = stmt.replace("numberlist", "double[]");
            stmt = stmt.replace("stringlist", "String[]");
            stmt = stmt.replace("number", "double");
            stmt = stmt.replace("string", "String");
            
            ret = stmt;
        }
        
        return ret;
    }
    
    /**
     * TODO: double check if the temporary holder has to be declared in foreach
     * @param stmt
     * @return 
     */
    public static boolean isForEach(String stmt){
        List<String> words = Arrays.asList(stmt.split(" +"));
        if(words.get(0).equals("foreach")){
            return true;
        }
        return false;
    }
    
    public static String convertForEachStmt(String stmt){
        List<String> words = Arrays.asList(stmt.split(" +"));
        String retval = "for(";
        String variableName = words.get(4);
        String listName = words.get(2);
        
        String hashDataType = (String)hashMap.get(listName);
        
//        System.out.println("listName is "+ listName);
//        System.out.println("hashDataType is "+ hashDataType);
        if(hashDataType.equals("double[]")){
            retval += "double ";
        }else if(hashDataType.equals("String[]")){
            retval += "String";
        } else {
            System.out.println(listName +" is not declared!");
            System.exit(0);
        }
        
        retval += variableName;
        retval += ": "+listName + "){";
        return retval;
    }
}