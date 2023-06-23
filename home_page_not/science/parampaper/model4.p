// M. C. Vanier and J. M. Bower
// A Comparative Survey of Automated Parameter-Searching Methods
// for Compartmental Neural Models

// Description of the second passive model (model 4).
// Morphology and target values of the passive parameters.

// RM: the specific membrane resistivity in units of ohm-(meter^2
// RA: the specific axial resistivity in units of ohm-meter
// CM: the specific membrane capacitance in units of farad/(meter^2)
// Em: passive membrane "reversal potential" in units of volts
// initVm: starting value of the resting potential in volts

*set_compt_param    Em      -0.077
*set_compt_param    initVm  -0.077

// The soma compartment is a sphere 20 microns in radius.

*set_compt_param    RM     0.2
*set_compt_param    RA     6.0
*set_compt_param    CM     0.008

*compt /library/compartment
*spherical

soma  none    0   0   0   20

// Each of the dendrites is a cylinder with a length of 125 microns
// and a diameter of 3 microns.  

*set_compt_param    RM     2.0
*set_compt_param    RA     6.0
*set_compt_param    CM     0.008
*compt /library/compartment
*cylindrical

dendrite1   soma    0   0   125   3

*set_compt_param    RM     0.2
*set_compt_param    RA     6.0
*set_compt_param    CM     0.008
*compt /library/compartment
*cylindrical

dendrite2   soma    0   0  -125   3

*set_compt_param    RM     2.0
*set_compt_param    RA     0.6
*set_compt_param    CM     0.008
*compt /library/compartment
*cylindrical

dendrite3   soma    0   125   0   3

*set_compt_param    RM     2.0
*set_compt_param    RA     6.0
*set_compt_param    CM     0.08
*compt /library/compartment
*cylindrical

dendrite4   soma    0  -125   0   3
