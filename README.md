# Keyword-Based Knowledge Graph Exploration Based on Diameter-Bounded Max-Coverage Group Steiner Trees

[![Contributions Welcome](https://img.shields.io/badge/Contributions-Welcome-brightgreen.svg?style=flat-square)](https://github.com/nju-websoft/OpenEA/issues)
[![License](https://img.shields.io/badge/License-Apache-lightgrey.svg?style=flat-square)](https://github.com/nju-websoft/OpenEA/blob/master/LICENSE)
[![language-python3](https://img.shields.io/badge/Language-Java-yellow.svg?style=flat-square)](https://www.python.org/)

This is the source code of the paper 'Keyword-Based Knowledge Graph Exploration Based on Diameter-Bounded Max-Coverage Group Steiner Trees'.

## Directory Structure

Directory /src contains all the source code based on JDK 11.

- Directory /src/HBLL contains our implementation of importing graph data and constructing pruned HBLL.

  'WeightedGraph' reads data from files.

  'Pruned_HBLL' constructs pruned HBLL.

  'PLL' constructs PLL (only for DBpedia and LUBM-5M in the experiments).

- Directory /src/method contains our implementation of CBA(+).

  'CertificateSearch' is our implementation of OptKVC(+).

  'GreedyGST' constructs an answer from a multiset of keyword vertices and a certificate returned by OptKVC(+).

  'CBAandCBAplus' is our implementation of CBA(+).

  'Run' is the main entrance. It calls our algorithms, including constructing HBLL, constructing PLL, and invoking CBA(+).

## Getting Started

### Environment

JDK11

### Build

```shell
cd CBA-main/src
javac method/Run.java
```

### Data

#### KGs and Queries

Our data is available from [![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.6778095.svg)](https://doi.org/10.5281/zenodo.6778095)

Unzip all zip files in the directory CBA-main/src/resource.

Directory CBA-main/src/resource contains all the data used in our experiments, including 5 real KGs (`MONDIAL`, `OpenCyc`, `LinkedMDB`, `YAGO`, and `DBpedia`) and 3 synthetic KGs (`LUBM-50K`, `LUBM-500K`, and `LUBM-5M`).

Each real KG directory contains 6 files, including:

- `graph.txt`: The first value is the number of vertices. Then each line 'u    v' means there is an undirected edge between 'u' and 'v'.
- `Weightgraph.txt` The first value is the number of vertices. Then each line 'u    v    w' means there is an undirected edge between 'u' and 'v' weighted by 'w' which is computed by the Informativeness-based Weighting (IW) scheme.
- `nodeName.txt`: Mapping from vertex ID to vertex name (i.e., entity URI).
- `query.txt`: Each line is a keyword query containing a set of keyword names.
- `kwName.txt`: Mapping from keyword ID to keyword name.
- `kwMap.txt`: Mapping from keyword ID to vertex IDs. The first value of each line is keyword ID, and the rest are vertex IDs.

Each synthetic directory contains 4 files, including:

- `graph.txt`: same as above.
- `Weightgraph.txt`: same as above.
- `nodeName.txt`: same as above.
- `queryList.txt`: Each line contains a set (separated by ',') of sets of keyword vertex IDs.

#### Generate HBLL and Hub Label

To run our algorithms, HBLL should be built ahead. Take  `MONDIAL`  as an example. 

Use following command to construct HBLL with Unit Weighting (UW):

```shell
java method/Run resource/Mondial HBLL UW
```

Then an index file `UWHBLL.txt` will be generated in directory CBA-main/src/resource/Mondial.

The command for Informativeness-based Weighting (IW) is similar:

```shell
java method/Run resource/Mondial HBLL IW
```

Then an index file `IWHBLL.txt` will be generated in directory CBA-main/src/resource/Mondial.

For `DBpedia` and `LUBM-5M`, PLL should also be constructed ahead of running our algorithms.  Take `DBpedia` as an example.

Use the following command to construct PLL for `DBpedia`:

```shell
java method/Run resource/Dbpedia PLL UW
```

Then an index file `PLLlabel.txt`  will be generated in directory CBA-main/src/resource/Dbpeida.

### Run Algorithm

Take MONDIAL as an example.

Run CBA for MONDIAL with Unit Weighting (UW):

```shell
java method/Run resource/Mondial CBA UW
```

Run CBA for MONDIAL with Informative-based Weighting (IW):

```shell
java method/Run resource/Mondial CBA IW
```

Run CBA+ for MONDIAL of Unit Weighting (UW):

```shell
java method/Run resource/Mondial CBA+ UW
```

Run CBA+ for MONDIAL of Informative-based Weighting (IW):

```shell
java method/Run resource/Mondial CBA+ IW
```

The outputs will be stored in directory CBA-main/src/resource/Mondial/UWresult/CBA (or IWresult/CBA, UWresult/CBA+, IWresult/CBA+).

## Citation

If you think our algorithms or our experimental results are useful, please kindly cite our paper.
