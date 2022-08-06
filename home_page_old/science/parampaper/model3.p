// M. C. Vanier and J. M. Bower
// A Comparative Survey of Automated Parameter-Searching Methods
// for Compartmental Neural Models

// Description of the first passive model (model 3).
// Morphology and target values of the passive parameters.

// RM: the specific membrane resistivity in units of ohm-(meter^2
// RA: the specific axial resistivity in units of ohm-meter
// CM: the specific membrane capacitance in units of farad/(meter^2)
// Em: passive membrane "reversal potential" in units of volts
// initVm: starting value of the resting potential in volts

*set_compt_param    RM       2.0
*set_compt_param    RA       6.0
*set_compt_param    CM       0.01
*set_compt_param    Em      -0.070
*set_compt_param    initVm  -0.070

// The dendrites is a cylinder with a length of 2000 microns
// and a diameter of 5 microns.  

*compt /library/compartment
*cylindrical

dendrite1   soma    0   0   2000   5

