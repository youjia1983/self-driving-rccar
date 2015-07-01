This page describe the kinematics model of the car.

# Bicycle model #

Also called in some places as the Ackerman steering model.


## System parameters ##

<a href='http://www.codecogs.com/eqnedit.php?latex=(x, y, \theta)'><img src='http://latex.codecogs.com/gif.latex?(x, y, \theta)%.png' title='(x, y, \theta)' /></a> is the car's coordinate and heading in the global coordinate system.

<a href='http://www.codecogs.com/eqnedit.php?latex=(d, \alpha)'><img src='http://latex.codecogs.com/gif.latex?(d, \alpha)%.png' title='(d, \alpha)' /></a> are the car's forward displacement and steer angle, respectively.

<a href='http://www.codecogs.com/eqnedit.php?latex=L'><img src='http://latex.codecogs.com/gif.latex?L%.png' title='L' /></a> is the car's length.


## Update equations ##

First compute the turn-angle and radius:

<a href='http://www.codecogs.com/eqnedit.php?latex=\beta = \frac{d}{L} \tan \alpha'><img src='http://latex.codecogs.com/gif.latex?\beta = \frac{d}{L} \tan \alpha%.png' title='turn angle' /></a>

<a href='http://www.codecogs.com/eqnedit.php?latex=R = \frac{d}{\beta }'><img src='http://latex.codecogs.com/gif.latex?R = \frac{d}{\beta}%.png' title='turn radius' /></a>


Then the turn-circle centres:

<a href='http://www.codecogs.com/eqnedit.php?latex=c_{x} = x - \sin (\theta)\cdot R'><img src='http://latex.codecogs.com/gif.latex?c_{x} = x - \sin (\theta)\cdot R%.png' title='turn x centre' /></a>

<a href='http://www.codecogs.com/eqnedit.php?latex=c_{y} = y @plus; \cos (\theta)\cdot R'><img src='http://latex.codecogs.com/gif.latex?c_{y} = y + \cos (\theta)\cdot R%.png' title='turn y centre' /></a>


Update the car coordinates and heading:

<a href="http://www.codecogs.com/eqnedit.php?latex=x' = c_{x} @plus; \sin (\theta @plus; \beta) \cdot R"><img src="http://latex.codecogs.com/gif.latex?x' = c_{x} + \sin (\theta + \beta) \cdot R%.png" title='new x' /></a>

<a href="http://www.codecogs.com/eqnedit.php?latex=y' = c_{y} - \cos (\theta @plus; \beta) \cdot R"><img src="http://latex.codecogs.com/gif.latex?y' = c_{y} - \cos (\theta + \beta) \cdot R%.png" title='new y' /></a>

<a href="http://www.codecogs.com/eqnedit.php?latex=\theta' = (\theta @plus; \beta) \textup{mod} 2\pi"><img src="http://latex.codecogs.com/gif.latex?\theta' = (\theta + \beta) \textup{mod} 2\pi%.png" title='new theta' /></a>


For a straight drive, <a href='http://www.codecogs.com/eqnedit.php?latex=\left | \beta \right | < \epsilon'><img src='http://latex.codecogs.com/gif.latex?\left | \beta \right | < \epsilon%.png' title='drive straight condition' /></a>:

<a href="http://www.codecogs.com/eqnedit.php?latex=x' = x @plus; d \cdot \cos \theta"><img src="http://latex.codecogs.com/gif.latex?x' = x + d \cdot \cos \theta%.png" title='new x straight' /></a>

<a href="http://www.codecogs.com/eqnedit.php?latex=y' = y @plus; d \cdot \sin \theta"><img src="http://latex.codecogs.com/gif.latex?y' = y + d \cdot \sin \theta%.png" title='new y straight' /></a>

<a href="http://www.codecogs.com/eqnedit.php?latex=\theta' = (\theta @plus; \beta) \textup{mod} 2\pi"><img src="http://latex.codecogs.com/gif.latex?\theta' = (\theta + \beta) \textup{mod} 2\pi%.png" title='new theta straight' /></a>


where <a href='http://www.codecogs.com/eqnedit.php?latex=\epsilon'><img src='http://latex.codecogs.com/gif.latex?\epsilon%.png' title='epsilon' /></a> a small number, e.g. 0.001.