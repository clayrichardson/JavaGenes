echo "testing crystal"
cd runDirectory
../../runJava.sh CrystalTest

echo BodyCenteredCubic.xyz
diff ../correctDirectory/BodyCenteredCubic.xyz BodyCenteredCubic.xyz

echo BodyCenteredCubicThreebodies.tsd
diff ../correctDirectory/BodyCenteredCubicThreebodies.tsd BodyCenteredCubicThreebodies.tsd

echo BodyCenteredCubicTwobodies.tsd
diff ../correctDirectory/BodyCenteredCubicTwobodies.tsd BodyCenteredCubicTwobodies.tsd

echo FaceCenteredCubic.xyz
diff ../correctDirectory/FaceCenteredCubic.xyz FaceCenteredCubic.xyz

echo FaceCenteredCubicThreebodies.tsd
diff ../correctDirectory/FaceCenteredCubicThreebodies.tsd FaceCenteredCubicThreebodies.tsd

echo FaceCenteredCubicTwobodies.tsd
diff ../correctDirectory/FaceCenteredCubicTwobodies.tsd FaceCenteredCubicTwobodies.tsd

echo SimpleCubicSmall.xyz
diff ../correctDirectory/SimpleCubicSmall.xyz SimpleCubicSmall.xyz

echo SimpleCubicSmallThreebodies.tsd
diff ../correctDirectory/SimpleCubicSmallThreebodies.tsd SimpleCubicSmallThreebodies.tsd

echo SimpleCubicSmallTwobodies.tsd
diff ../correctDirectory/SimpleCubicSmallTwobodies.tsd SimpleCubicSmallTwobodies.tsd

echo SimpleCubic.xyz
diff ../correctDirectory/SimpleCubic.xyz SimpleCubic.xyz

echo SimpleCubicThreebodies.tsd
diff ../correctDirectory/SimpleCubicThreebodies.tsd SimpleCubicThreebodies.tsd

echo SimpleCubicTwobodies.tsd
diff ../correctDirectory/SimpleCubicTwobodies.tsd SimpleCubicTwobodies.tsd

echo Diamond.xyz
diff ../correctDirectory/Diamond.xyz Diamond.xyz

echo DiamondThreebodies.tsd
diff ../correctDirectory/DiamondThreebodies.tsd DiamondThreebodies.tsd

echo DiamondTwobodies.tsd
diff ../correctDirectory/DiamondTwobodies.tsd DiamondTwobodies.tsd



