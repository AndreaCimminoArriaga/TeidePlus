# Teide+

We present Teide+, an hybrid approach to improve the precision of linking resources from the Web of Data relaying on link rules (which are generally obtained by means of genetic programming algorithms). Teide+ receives a base link rule (main link rule) and apply it in order to obtain a collection of candidate links. Then a set of supporting link rules are bootstrapped to analyse the neighbours of the resources involved in each candidate link (the neighbours are the resources that can be reached by means of their object properties and linked by a supporting link rule). Teide analyses how similar such set of neighbours are in order decide which of the candidate links must be kept or discarded as false positives. In addition, it relies on two voting heuristics to prune selected links increasing precision of links generated.

## Downloading Teide+

Find in this project a TeidePlus.jar. 

## Running an experiment

Sample experiments can be found at https://goo.gl/Pu76SU. Download the file and unzip a scenario, inside there are required steps to obtain the results.


## Authors
 * **[Andrea Cimmino](http://www.tdg-seville.info/acimmino/Home)**

## License

This project is licensed under TDG's License - visit http://www.tdg-seville.info/license.html for details

## Acknowledgments
 * Special thanks to [Dr. Rafael Corchuelo](http://www.tdg-seville.info/corchu/Home)
 * Spanish R&D programme (grants TIN2013-40848-R and TIN2013-40848-R)
