// M. C. Vanier and J. M. Bower
// A Comparative Survey of Automated Parameter-Searching Methods
// for Compartmental Neural Models

// Description of the simple one-compartment cell models
// (models 1 and 2).  Morphology, passive parameters, and target
// values of the channel conductance densities.

// RM: the specific membrane resistivity in units of ohm-(meter^2
// RA: the specific axial resistivity in units of ohm-meter
// CM: the specific membrane capacitance in units of farad/(meter^2)
// Em: passive membrane "reversal potential" in units of volts
// initVm: starting value of the resting potential in volts

*set_compt_param    RM      2.0
*set_compt_param    RA      6.0
*set_compt_param    CM      0.008
*set_compt_param    Em     -0.077
*set_compt_param    intiVm -0.077

*compt /library/compt
*spherical

// Format: compartment_name parent_name x y z diameter 
//    [Channel density]...
//
// x, y, z, and diameter coordinates are in microns.
// This cell is a sphere with a radius of 20 microns.
// Channel densities represent the maximum conductance of the
// channel per unit area, and are in units of siemens/(meter^2) 


soma  none        0   0   0   20        \
          Na_hip_traub91      800.0     \
          Kdr_hip_traub91     100.0     \
          Ka_hip_traub91       50.0     \
          KM_bsg_yka          100.0


