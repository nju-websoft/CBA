# Keyword-Based Knowledge Graph Exploration Based on Diameter-Bounded Max-Coverage Group Steiner Trees

[![Contributions Welcome](https://img.shields.io/badge/Contributions-Welcome-brightgreen.svg?style=flat-square)](https://github.com/nju-websoft/OpenEA/issues)
[![License](https://img.shields.io/badge/License-Apache-lightgrey.svg?style=flat-square)](https://github.com/nju-websoft/OpenEA/blob/master/LICENSE)
[![language-python3](https://img.shields.io/badge/Language-Java-yellow.svg?style=flat-square)](https://www.python.org/)

This is the source code of the paper 'Keyword-Based Knowledge Graph Exploration Based on Diameter-Bounded Max-Coverage Group Steiner Trees'.

## Directory Structure

Directory /src contains all the source code based on JDK 11.

- Directory /src/HBLL includes some classes to build pruned HBLL and read data

  'WeightedGraph' is used to read data from files

  'Pruned_HBLL' is used to construct pruned HBLL

  'PLL' is our implementation of PLL to build Hub Label used in CBA/CBA+ on Dbpedia and LUBM-5M 

- Directory /src/method contains our implementation of algorithms CBA and CBA+

  'CertificateSearch' is implementation of OptKVC and OptKVC+

  'GreedyGST' is used to build trees from multiset and certificate

  'CBAandCBAplus' is used to generate answers of CBA/CBA+

  'Run' is used to call our algorithms, including PLL, HBLL, CBA and CBA+

## Getting Started

### Environment

JDK11

### Build

```shell
cd CBA-main/src
javac method/Run.java
```

### Data

#### Dataset

Our dataset is available from 

Unzip the resource.zip in the directory CBA-main/src.

Directory CBA-main/src/resource contains the dataset used in our experiment, including 5 real KGs (`Mondial`, `OpenCyc`, `LinkedMDB`, `YAGO` and `Dbpedia`) and 3 LUBM synthetic KGs (`LUBM-50K`, `LUBM-500K` and `LUBM-5M`).

Real KG directory contains 6 files, including:

- `graph.txt`: the node ID graph extracted from dataset. The first value of `graph.txt` is the number of nodes. Then each line 'u    v' means there is an undirected edge between 'u' and 'v'.
- `Weightgraph.txt` the node ID weight graph extracted from dataset.  The first value of `Weightgraph.txt` is the number of nodes. Then each line 'u    v    w' means there is an undirected 'w'-weight edge between 'u' and 'v'. Notice that 'w' is calculated on Informativeness-based Weighting scheme.
- `nodeName.txt`: the mapping relation from node ID to node name.
- `kwName.txt`: the mapping relation from keyword ID to keyword name.
- `kwMap.txt`: the containing relation from keyword ID to node ID. The first value of each line is keyword ID and others are node ID.
- `query.txt`: the keyword queries used in experiments. Each line means a keyword query containing a set of keyword names.

LUBM synthetic directory contains 4 files, including:

- `graph.txt`: same as it is in real KG directory.
- `Weightgraph.txt`: same as it is in real KG directory.
- `nodeName.txt`: same as it is in real KG directory.
- `queryList.txt`: the node group queries used in experiments. Each line contains a set of node groups separated by ','.

#### Generate HBLL and Hub Label

To run our algorithms, HBLL should be built ahead. Take  `Mondial`  as an example. 

Use following command to construct HBLL of Unit Weighting:

```shell
java method/Run resource/Mondial HBLL UW
```

Then the Unit Weighting HBLL file `UWHBLL.txt` will be generated in directory CBA-main/src/resource/Mondial.

The constructing HBLL command of Informativeness-based Weighting is similar as that of Unit Weighting:

```shell
java method/Run resource/Mondial HBLL IW
```

Then the Informative-based Weighting HBLL file `IWHBLL.txt` will be generated in directory CBA-main/src/resource/Mondial.

For `Dbpedia` and `LUBM-5M`, Hub Label should also be constructed ahead to run our algorithms.  Take `Dbpedia` as an example.

Use the following command to construct Hub Label for `Dbpedia`:

```shell
java method/Run resource/Dbpedia PLL UW
```

Then the Hub Label file `PLLlabel.txt`  will be generated in directory CBA-main/src/resource/Dbpeida. Notice that changing weighting scheme  to `IW` will not influence this command.

### Run Algorithm

Take Mondial as an example.

Run CBA for Mondial of Unit Weighting:

```shell
java method/Run resource/Mondial CBA UW
```

Run CBA for Mondial of Informative-based Weighting:

```shell
java method/Run resource/Mondial CBA IW
```

Run CBA+ for Mondial of Unit Weighting:

```shell
java method/Run resource/Mondial CBA+ UW
```

Run CBA+ for Mondial of Informative-based Weighting:

```shell
java method/Run resource/Mondial CBA+ IW
```

The running results will be stored in directory CBA-main/src/resource/Mondial/UWresult/CBA (IWresult/CBA, UWresult/CBA+, IWresult/CBA+).

## License

This project is licensed under the GPL License - see the [LICENSE](LICENSE) file for details

## Citation

If you think our algorithms or our experimental results are useful, please kindly cite our paper.