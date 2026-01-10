# Tâche 1 : Questions sur la base de code

## 1. Rôle de l’interface `Projector` dans le système de gestion des événements

L’interface `Projector` joue un rôle central dans le mécanisme de projection des événements. Elle définit le contrat permettant de transformer un événement métier en une modification d’une vue matérialisée.
Dans une architecture orientée événements et fondée sur le principe **CQRS**, le `Projector` est responsable de la traduction des événements issus du domaine en représentations optimisées pour la lecture. Il agit ainsi comme un adaptateur entre le flux d’événements et les modèles de lecture persistés.

## 2. Rôle du type générique `S` dans l’interface `Projector`

Le type générique `S` représente l’état de la projection, c’est-à-dire la vue matérialisée ou le modèle de lecture qui est mis à jour à partir des événements.
Il permet de rendre l’interface `Projector` générique et indépendante d’un modèle de projection spécifique, favorisant ainsi la réutilisabilité et la flexibilité du mécanisme de projection.

## 3. Complément de la Javadoc de l’interface `Projector`

Dans la Javadoc de l’interface `Projector`, le type `S` peut être décrit comme suit :

> `S` représente le type de l’état de projection, correspondant à la vue matérialisée ou au modèle de lecture mis à jour lors de l’application d’un événement.

## 4. Intérêt d’utiliser une interface `Projector` plutôt qu’une classe concrète

L’utilisation d’une interface plutôt que d’une classe concrète permet :
*   de découpler le mécanisme de projection de ses implémentations,
*   de faciliter l’extension du système par l’ajout de nouvelles projections,
*   d’améliorer la testabilité en permettant le remplacement des implémentations par des doublures (mocks ou stubs),
*   de respecter les principes SOLID, en particulier le principe de substitution de Liskov et le principe d’inversion des dépendances.

## 5. Rôle de la classe `ProjectionResult` dans l’interface `Projector`

La classe `ProjectionResult` encapsule le résultat de l’application d’un événement sur une projection.
Elle permet de représenter soit un succès (avec un nouvel état de projection), soit un échec (avec une information d’erreur), sans recourir à la levée d’exceptions immédiates.
Cette approche s’inscrit dans une logique fonctionnelle et prépare l’usage du concept de **monade**.

## 6. Intérêt de l’usage d’une monade par rapport à la gestion d’erreur traditionnelle

L’usage d’une monade pour la gestion des erreurs présente plusieurs avantages par rapport au modèle traditionnel basé sur les exceptions :
*   Les erreurs deviennent des valeurs explicites, ce qui améliore la lisibilité et la prévisibilité du code.
*   Le flux de traitement est plus clair, car les cas de succès et d’échec sont traités de manière uniforme.
*   La composition des opérations est facilitée, notamment dans les chaînes de traitements successifs.
*   Le recours excessif aux blocs `try/catch` est évité, ce qui réduit la complexité et le bruit syntaxique.

Dans le contexte de la projection d’événements, l’approche monadique permet de chaîner proprement les projections tout en gérant les erreurs de manière contrôlée et explicite.

# Tâche 2 : Questions concernant l’Outboxing

## 1. Rôle de l’interface `OutboxRepository`

L’interface `OutboxRepository` est responsable de la persistance des événements destinés à être publiés vers des systèmes externes.
Elle constitue le point d’accès au stockage intermédiaire des événements, appelé **outbox**, garantissant que les événements produits par le domaine sont conservés de manière fiable avant leur diffusion.

## 2. Garantie de livraison des événements grâce à l’Outbox Pattern

L’**Outbox Pattern** permet de garantir la livraison des événements en assurant que :
*   l’écriture de l’événement et la modification de l’état métier sont réalisées dans la même transaction,
*   les événements sont stockés de manière persistante avant leur publication,
*   la publication peut être rejouée en cas d’échec temporaire.

Cette approche élimine les incohérences entre l’état métier et la diffusion des événements dans un système distribué.

## 3. Fonctionnement concret de l’Outbox Pattern dans l’application

Dans l’application, le fonctionnement est le suivant :
1.  Une commande métier est traitée.
2.  L’état métier est modifié dans la base de données.
3.  L’événement correspondant est enregistré dans la table d’outbox dans la **même transaction**.
4.  Un composant dédié lit périodiquement les événements de l’outbox.
5.  Les événements sont publiés vers le système de messagerie ou les consommateurs.
6.  Une fois publiés, les événements sont marqués comme traités.

## 4. Gestion des erreurs de livraison

En cas d’erreur de livraison, les événements restent stockés dans l’outbox avec un statut indiquant qu’ils n’ont pas encore été traités. Les implémentations concrètes peuvent ainsi rejouer la livraison jusqu’à ce qu’elle aboutisse, assurant une résilience élevée.

# Tâche 3 : Questions concernant le journal d’événements

## 1. Rôle du journal d’événements

Le journal d’événements constitue une trace **immuable** de tous les événements produits par le système. Il sert de source de vérité historique permettant de comprendre l’évolution de l’état métier dans le temps.

## 2. Justification de la méthode unique `append`

L’interface `EventLogRepository` ne propose qu’une méthode `append` car le journal d’événements est conçu comme une structure en **écriture seule**. Les événements ne sont ni modifiés ni supprimés afin de garantir leur immutabilité et leur intégrité.

## 3. Implications de cette conception

Cette conception implique :
*   une traçabilité complète des changements,
*   la possibilité de rejouer les événements pour reconstruire des projections,
*   l’utilisation du journal à des fins d’audit ou d’analyse.

# Tâche 4 : Limites de CQRS

## 1. Principales limites de CQRS

Les principales limites de CQRS dans ce contexte sont :
*   l’augmentation de la complexité architecturale,
*   la gestion de la cohérence éventuelle entre les modèles de lecture et d’écriture,
*   la difficulté accrue de débogage et de supervision.

## 2. Limites déjà compensées par l’implémentation actuelle

L’application compense certaines limites par :
*   l’usage d’événements persistés,
*   l’**Outbox Pattern** pour garantir la fiabilité,
*   une séparation claire des responsabilités.

## 3. Limites introduites par la mise en œuvre actuelle

La multiplication des projections et des composants techniques peut introduire :
*   des coûts opérationnels supplémentaires,
*   une latence accrue entre écriture et lecture,
*   une complexité de maintenance plus élevée.

## 4. Cas d’une projection multiple

Dans le cas d’une projection multiple, un même événement peut déclencher plusieurs projections indépendantes. Cela implique une gestion fine des erreurs afin d’éviter qu’une projection en échec n’empêche les autres de s’exécuter correctement.

## 5. Solutions pour atténuer les limites (Bonus)

Plusieurs solutions peuvent être envisagées :
*   mise en place de mécanismes de supervision et de monitoring,
*   gestion explicite des erreurs et des rejets de projections,
*   parallélisation contrôlée des projections,
*   documentation et standardisation des projections.
