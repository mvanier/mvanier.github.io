// M. C. Vanier and J. M. Bower
// A Comparative Survey of Automated Parameter-Searching Methods
// for Compartmental Neural Models

// Description of the simple one-compartment cell model
// (models 1 and 2).  Active conductances and reversal potentials.
// Channel descriptions are in the GENESIS neural modeling language;
// for more information see "The Book of GENESIS, 2nd. Ed." by J. M. 
// Bower and David Beeman, Springer/Telos.  All voltage- or calcium-
// dependent ionic channels are very standard Hodgkin-Huxley-type
// channels.

// genesis

// CONSTANTS:

// channel equilibrium potentials (V)
float   EREST_ACT
float   ENA
float   EK 


//========================================================================
//                      Passive properties (for channels only)
//========================================================================

function make_passive
    EREST_ACT   = -0.060   
    ENA         =  0.055
    EK          = -0.090
end



//========================================================================
//                Tabchannel Hippocampal fast Na channel
//========================================================================

function make_Na_hip_traub91
    if ({exists Na_hip_traub91})
        return
    end

    create  tabchannel      Na_hip_traub91
    setfield             ^       \
        Ek              {ENA}    \    // Volts
        Ik              0        \    // Amperes
        Gk              0        \    // Siemens
        Xpower  2       \
        Ypower  1       \
        Zpower  0

    setupalpha  Na_hip_traub91  X            \
        {320e3  * (0.0131 + EREST_ACT)}      \
        -320e3                               \
        -1.0                                 \
        {-1.0 * (0.0131 + EREST_ACT)}        \
        -0.004                               \
        {-280e3 * (0.0401 + EREST_ACT)}      \
        280e3                                \  
        -1.0                                 \
        {-1.0   * (0.0401 + EREST_ACT)}      \
        5.0e-3

    setupalpha  Na_hip_traub91  Y        \
        128.0                            \              
        0.0                              \
        0.0                              \
        {-1.0 * (0.017 + EREST_ACT)}     \
        0.018                            \
        4.0e3                            \
        0.0                              \
        1.0                              \
        {-1.0 * (0.040 + EREST_ACT)}     \
        -5.0e-3
end


//========================================================================
//                Tabchannel K(DR) Hippocampal cell channel
//========================================================================

function make_Kdr_hip_traub91
    if ({exists Kdr_hip_traub91})
        return
    end

    create  tabchannel  Kdr_hip_traub91
    setfield    ^       \
        Ek      {EK}    \              
        Ik      0       \              
        Gk      0       \              
        Xpower  1       \
        Ypower  0       \
        Zpower  0

    setupalpha Kdr_hip_traub91 X \
        {16e3 * (0.0351 + EREST_ACT)}    \
        -16e3                            \
        -1.0                             \
        {-1.0 * (0.0351 + EREST_ACT)}    \
        -0.005                           \
        250                              \
        0.0                              \ 
        0.0                              \
        {-1.0 * (0.02 + EREST_ACT)}      \
        0.04 

    scaletabchan Kdr_hip_traub91  X tau 1.0 0.27 0.0 0.0 
end



//========================================================================
//                Tabchannel K(A) Hippocampal cell channel
//========================================================================

function make_Ka_hip_traub91
    if ({exists Ka_hip_traub91})
        return
    end

    create  tabchannel      Ka_hip_traub91
    setfield    ^       \
        Ek      {EK}    \             
        Ik      0       \             
        Gk      0       \             
        Xpower  1       \
        Ypower  1       \
        Zpower  0

    setupalpha Ka_hip_traub91 X   \                                   
        {20e3 * (0.0131 + EREST_ACT)}          \
        -20e3                                  \
        -1.0                                   \
        {-1.0 * (0.0131 + EREST_ACT) }         \
        -0.01                                  \
        {-17.5e3 * (0.0401 + EREST_ACT) }      \
        17.5e3                                 \
        -1.0                                   \
        {-1.0 * (0.0401 + EREST_ACT) }         \
        0.01                                   

    setupalpha Ka_hip_traub91 Y                \
        1.6                                    \
        0.0                                    \
        0.0                                    \
        {0.013 - EREST_ACT}                    \
        0.018                                  \
        50.0                                   \
        0.0                                    \
        1.0                                    \
        {-1.0 * (0.0101 + EREST_ACT) }         \
        -0.005 

    scaletabchan Ka_hip_traub91  Y tau 1.0 0.05 0.0 0.0 
end



//========================================================================
//            Non-inactivating Muscarinic K current    
//========================================================================

function make_KM_bsg_yka
    if ({exists KM_bsg_yka})
        return
    end

    int i
    float x, dx, y

    create  tabchannel  KM_bsg_yka
    setfield  KM_bsg_yka  \
         Ek     {EK}      \
         Gbar   0         \
         Ik     0         \
         Gk     0         \
         Xpower 1         \
         Ypower 0         \
         Zpower 0

    call KM_bsg_yka TABCREATE X 49 -0.1 0.05
    x = -0.1
    dx = 0.15 / 49.0

    for (i = 0 ; i <= 49 ; i = i + 1)
        y = 1.0 / (3.3 * {exp {(x + 0.035)/0.02}} + \
                         {exp {-(x + 0.035)/0.02}})
        setfield KM_bsg_yka X_A->table[{i}] {y}

        y = 1.0 / (1.0 + {exp {-(x + 0.035)/0.01}})
        setfield KM_bsg_yka X_B->table[{i}] {y}
        x = x + dx
    end

    tweaktau KM_bsg_yka X 
    setfield  KM_bsg_yka X_A->calc_mode 0 X_B->calc_mode 0 
    call KM_bsg_yka TABFILL X 3000 0

    scaletabchan KM_bsg_yka  X tau 1.0 0.5 0.0 0.0 
end


//========================================================================
//                Make library of prototypes
//========================================================================

function delete_channel_library  
    ce /library
    call Na_hip_traub91      TABDELETE
    call Kdr_hip_traub91     TABDELETE
    call Ka_hip_traub91      TABDELETE
    call KM_bsg_yka          TABDELETE
    ce / 
    delete /library 
end

function make_channel_library
    if({exists /library})
        delete_channel_library
    end
    create neutral /library
    ce /library
    create compartment compt
    make_passive
    make_Na_hip_traub91
    make_Kdr_hip_traub91
    make_Ka_hip_traub91
    make_KM_bsg_yka 
    ce /
    disable /library
end

