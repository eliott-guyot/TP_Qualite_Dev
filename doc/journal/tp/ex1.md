# Tâche 1 : Domaines métiers et responsabilités

## 1.1 Domaines métiers principaux

L’application **Order Flow** est conçue selon une architecture orientée services (SOA) et s’appuie sur les principes de la conception pilotée par le domaine (**DDD**).
L’analyse des fichiers de cartographie de contexte permet d’identifier les principaux domaines métiers suivants :

*   **Gestion des produits (Product Registry)** : Ce domaine est responsable de la définition, de la gestion et de l’exposition des informations relatives aux produits.
*   **Gestion des commandes / Store** : Ce domaine couvre les interactions liées à la consultation du catalogue, au passage de commandes et à la gestion des flux associés.
*   **Infrastructure et support transverse** : Ce domaine regroupe les mécanismes techniques communs tels que la persistance, la gestion des événements, le support CQRS et les modèles métier partagés.

## 1.2 Conception des services et domaines métiers

Chaque service de l’application est conçu pour implémenter un domaine métier unique ou une responsabilité technique transverse.
L’architecture repose sur les principes suivants :
*   Découpage en microservices alignés sur les **bounded contexts** DDD.
*   Séparation stricte entre les responsabilités de lecture et d’écriture (**CQRS**).
*   Mutualisation des éléments techniques communs via des bibliothèques partagées.

La structure des dossiers `apps` et `libs` reflète directement cette séparation des responsabilités.

## 1.3 Responsabilités des modules

### Modules applicatifs (`apps/`)

*   **`apps/store-back`** : Implémente le backend du domaine **Store**. Il expose des API REST destinées au frontend, traite les commandes métier et publie des événements correspondant aux changements d’état du domaine.
*   **`apps/store-front`** : Correspond à l’interface utilisateur de l’application. Il se limite à la présentation et à l’orchestration des appels vers les services backend, sans contenir de logique métier significative.
*   **`apps/product-registry-domain-service`** : Représente la partie **write** du domaine **Product Registry**. Il implémente les règles métier, les agrégats et les commandes associées à la gestion des produits, et publie les événements de domaine correspondants.
*   **`apps/product-registry-read-service`** : Correspond à la partie **read** du domaine **Product Registry**. Il est dédié à la consultation des données et expose des modèles optimisés pour la lecture, sans contenir de logique métier complexe.

### Bibliothèques partagées (`libs/`)

*   **`libs/kernel`** : Constitue le noyau métier partagé de l’application. Il contient les objets de valeur, les identités métier, les événements de domaine et les exceptions métier. Il garantit la cohérence des règles métier communes.
*   **`libs/bom-platform`** : Fournit une **Bill of Materials (BOM)** permettant de centraliser et d’harmoniser les versions des dépendances utilisées dans l’ensemble du projet.
*   **`libs/cqrs-support`** : Fournit une implémentation générique des mécanismes **CQRS**. Il met à disposition les abstractions nécessaires à la gestion des commandes, des requêtes et des événements.
*   **`libs/sql`** : Regroupe les éléments liés à la persistance SQL, tels que la configuration des bases de données et les mécanismes d’accès aux données communs.

# Tâche 2 : Identifier les concepts principaux

## 2.1 Concepts utilisés dans l’application

L’application Order Flow repose sur plusieurs concepts architecturaux et métiers majeurs :

*   **Domain-Driven Design (DDD)** : Utilisation de bounded contexts, d’agrégats, d’objets de valeur et d’événements de domaine.
*   **CQRS (Command Query Responsibility Segregation)** : Séparation stricte entre les opérations de modification de l’état (commandes) et les opérations de consultation (requêtes).
*   **Architecture orientée événements (EDA)** : Les changements d’état métier sont matérialisés par des événements permettant une communication asynchrone entre services.
*   **Persistance des données** : Stockage dans des bases SQL, avec une gestion transactionnelle côté écriture et des modèles dédiés à la lecture.
*   **Gestion des erreurs** : Les erreurs métier sont explicitement modélisées, tandis que les erreurs techniques sont isolées dans les couches d’infrastructure.
*   **Communication inter-services** : Les échanges se font via des API REST et via des événements métier.

## 2.2 Implémentation des concepts dans les modules

Les concepts métier sont principalement implémentés dans les modules applicatifs (`apps`), tandis que les mécanismes techniques sont mutualisés dans les bibliothèques (`libs`). L’approche privilégie l’utilisation de solutions génériques pour éviter la duplication.

## 2.3 Rôle de la bibliothèque `libs/cqrs-support`

La bibliothèque `libs/cqrs-support` fournit l’infrastructure nécessaire à l’implémentation du pattern **CQRS**. Elle définit les abstractions permettant de traiter les commandes, les requêtes et les événements, laissant aux services métier la responsabilité de la logique fonctionnelle.

## 2.4 Rôle de la bibliothèque `libs/bom-platform`

La bibliothèque `libs/bom-platform` centralise la gestion des versions des dépendances. Elle garantit l’alignement des versions entre les différents modules et réduit les risques de conflits.

## 2.5 Fiabilité des états internes (CQRS et Kernel)

La fiabilité des états internes est assurée par :
*   L’encapsulation de l’état dans des agrégats métier.
*   L’utilisation d’événements comme mécanisme de propagation des changements.
*   La présence d’un noyau métier (`kernel`) imposant des types forts et des règles communes.
*   La séparation stricte entre lecture et écriture apportée par CQRS.
