java-boggle-solver
==================

Boggle solver written in Java.

It takes a boggle grid as input in the format EVNI,PBAE,IVEF,AUNL or generates a real boggle grid at random if input is not provided.

Supports parallel processing to split up the searching tasks. Filters the dictionary prior to searching by using the available letters on the boggle grid. The included dictionary is used as default, but you can provide the path to your own dictionary as a second command line argument. 

Example Usage:
--------------
Parameters:

    -d=/path/to/dictionary (Specify a dictionary to use)
    -b=EVNI,PBAE,IVEF,AUNL (Specify any input grid to solve)
    -s=5 (Specify the size of a grid to generate randomly and solve)
    ./run.sh --s=5 --multi --d=/usr/share/dict/words
    ./run.sh --multi -b=EVNI,PBAE,IVEF,AUNL
    
Example Output:
---------------
    ./run.sh -b=EVNIA,PBAEB,IVEFC,AUNLJ,CEOLS
    Removed 171781 words from dictionary. Remaining: 6910
    Filtered dictionary words in 0.079s
    Num Processors: 4

    Found all words in 0.105s
    [E   V   N   I   A]
    [P   B   A   E   B]
    [I   V   E   F   C]
    [A   U   N   L   J]
    [C   E   O   L   S]

    Found 109 words
    ACE AEON AIN ANE ANI AVA AVE AVENUE BAN BANE BANI BEAN BEANIE BEE BEEF BEEN BEFELL BEFLEA BEL BELL BELLS BELON BELS BEN BENE BIN BINE CAVE CEE CEIBA CLEAN CLEAVE CLEF CLON CLONE CUE CUVEE EAU EAVE ECU EEL EELS ELF ELL ELLS ELS ENOL ENOLS EON FAB FAIN FAN FANE FAVA FAVE FEE FEEB FEEL FEELS FELL FELLOE FELLS FELON FEN FEU FLEA FLEE FLOE LEA LEAF LEAN LEAVE LEE LENO LEU LEV LEVA LONE NAB NABE NAE NAEVI NAVE NAVEL NAVELS NEB NEE NEVI NIB OLE OLEA ONE PIA PIU SLEAVE SLOE UVEA VAC VACUOLE VAIN VAN VANE VAU VAV VEE VEENA VENUE VIA VIBE 

Using Boggle Solver in your own project
==================
The Solver class is designed to be used in any project. Simply use the call the public methods to create a boggle board randomly (or provide the input characters) and then call loadDictionary(). Then call run(). 

License
==================
GPLv2
