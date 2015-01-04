java-boggle-solver
==================

Boggle solver written in Java.

It takes a boggle grid as input in the format EVNI,PBAE,IVEF,AUNL or generates a real boggle grid at random if input is not provided.

Supports parallel processing to split up the searching tasks. Filters the dictionary prior to searching by using the available letters on the boggle grid. The included dictionary is used as default, but you can provide the path to your own dictionary as a second command line argument. 

Other optimizations are possible in the dictionary searching part, but it's pretty good for now.

Example Usage:
--------------

    java -classpath ./out/production/BoggleSolver net.butlerpc.boggle.Main EVNI,PBAE,IVEF,AUNL
    
Or use the including shell script helper to run it:

    ./run.sh
    ./run.sh EVNI,PBAE,IVEF,AUNL
    ./run.sh EVNIA,PBAEB,IVEFC,AUNLJ,CEOLS
    
Example Output:
---------------
    ./run.sh EVNIA,PBAEB,IVEFC,AUNLJ,CEOLS
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


License
==================
GPLv2
