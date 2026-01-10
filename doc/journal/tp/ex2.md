# Tâche 5

## 1. Différence entre tests unitaires et tests d’intégration

Les tests unitaires et les tests d’intégration constituent deux niveaux complémentaires de la stratégie de test d’une application logicielle.

**Les tests unitaires** ont pour objectif de valider le comportement d’une unité de code prise isolément, telle qu’une classe ou une méthode. Ils se concentrent principalement sur la logique interne et les règles métier, indépendamment de toute infrastructure technique. Ces tests doivent être rapides, déterministes et reproductibles.

**Les tests d’intégration**, quant à eux, visent à vérifier le bon fonctionnement des interactions entre plusieurs composants du système. Ils s’exécutent dans un environnement proche des conditions réelles d’exécution et permettent de valider l’intégration entre les différentes couches de l’application (web, application, infrastructure).

## 2. Pertinence d’une couverture de tests à 100 %

Bien qu’une couverture de tests élevée soit généralement souhaitable, il n’est pas toujours pertinent de viser systématiquement une couverture de code à 100 %.

Une couverture complète est particulièrement utile pour le cœur métier de l’application, où les règles fonctionnelles sont critiques et susceptibles d’évoluer. En revanche, chercher à tester l’intégralité du code peut conduire à des effets contre-productifs, tels que :
*   l’écriture de tests peu significatifs ou redondants,
*   le test de code purement technique ou généré automatiquement,
*   une augmentation de la complexité et du coût de maintenance des tests.

Ainsi, la couverture de tests doit être considérée comme un indicateur de qualité et non comme un objectif en soi. Une stratégie de test pertinente consiste à prioriser la couverture de la logique métier et des cas à forte valeur fonctionnelle, tout en limitant les tests sur les couches techniques ou les éléments à faible risque.

## 3. Apports de l’architecture en couches d’oignon pour la couverture des tests

L’architecture en couches d’oignon (Onion Architecture) favorise fortement la testabilité et la robustesse d’une application.

Ce modèle architectural repose sur un principe fondamental : le cœur métier de l’application ne dépend d’aucune infrastructure technique. Les dépendances sont orientées de l’extérieur vers l’intérieur, ce qui garantit l’indépendance du domaine métier.

Dans le cadre de la tâche 3, cette architecture a permis d’écrire des tests unitaires pour la classe `Product` sans nécessiter :
*   de configuration spécifique du framework,
*   d’accès à une base de données,
*   de mise en place d’un serveur applicatif.

Les principaux avantages observés sont :
*   une isolation complète de la logique métier,
*   une simplification significative de l’écriture des tests,
*   une réduction du recours au mocking complexe,
*   une meilleure fiabilité et une plus grande rapidité d’exécution des tests unitaires.

Ainsi, l’architecture en couches d’oignon constitue un levier essentiel pour obtenir une couverture de tests efficace, ciblée et durable.

## 4. Explication de la nomenclature des packages

La nomenclature des packages de l’application Order Flow reflète une séparation claire des responsabilités, conformément aux principes de l’architecture en couches.

*   **model** : Ce package contient les éléments centraux du domaine métier, tels que les entités, les agrégats, les objets de valeur et les règles métier. Il est indépendant de toute technologie ou framework.

*   **application** : Le package `application` regroupe la logique applicative, incluant les cas d’usage, l’orchestration des commandes et des requêtes, ainsi que la coordination entre le domaine et les couches externes.

*   **infra** : Le package `infra` contient les implémentations techniques concrètes, notamment l’accès aux systèmes externes, les adaptateurs et les détails d’infrastructure. Cette couche dépend des technologies utilisées.

*   **jpa** : Ce package est spécialisé dans la persistance des données via JPA. Il regroupe les entités de persistance, les repositories et les mécanismes de mapping entre les objets du domaine et la base de données relationnelle.

*   **web** : Le package `web` correspond à la couche de présentation et d’exposition de l’application. Il contient les ressources REST, les contrôleurs HTTP et la gestion des échanges avec les clients.

*   **client** : Le package `client` regroupe les composants responsables de la communication sortante vers d’autres services ou systèmes externes, tels que les clients REST ou les adaptateurs réseau.
