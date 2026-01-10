# Tâche 5 : Analyse conceptuelle et architecturale

## 1. Limites de l’implémentation actuelle du `ProjectionDispatcher`

L’implémentation actuelle du `ProjectionDispatcher` repose sur un modèle fortement simplifié dans lequel un agrégat est associé à une unique vue et à un seul gestionnaire de projection. Bien que ce modèle soit suffisant pour un cas d’usage simple, il présente plusieurs limites structurelles.

### a) Cas d’un agrégat avec plusieurs vues

Dans une architecture CQRS, il est courant qu’un même agrégat alimente plusieurs vues matérialisées (ex: vue détaillée, vue synthétique).
L’implémentation actuelle ne permet pas de gérer efficacement ce cas, car elle suppose une relation univoque entre un agrégat et une vue. Cette contrainte limite l’évolutivité du système.

### b) Cas d’une vue alimentée par plusieurs agrégats

Certaines vues matérialisées nécessitent l’agrégation d’événements provenant de plusieurs agrégats distincts.
L’implémentation actuelle ne permet pas de coordonner proprement les projections pour ce cas, ce qui complique la gestion de la cohérence des données et limite la capacité à modéliser des vues transverses.

### c) Cas de multiples gestionnaires d’événements distribués

Dans un système distribué, un même événement peut déclencher plusieurs traitements indépendants (mise à jour de vues, actions techniques, etc.).
L’implémentation actuelle centralisée ne permet pas de gérer efficacement :
*   la multiplicité des gestionnaires,
*   leur distribution sur plusieurs instances,
*   la tolérance aux pannes partielles.

## 2. Propositions d’améliorations structurelles

Pour lever les limitations identifiées, plusieurs améliorations structurelles peuvent être envisagées.

### a) Découplage entre événements, projections et gestionnaires

Il est recommandé d’introduire :
*   une abstraction explicite représentant un `ProjectionHandler`,
*   un mécanisme de dispatch dynamique basé sur le type d’événement,
*   une relation de type **un événement → plusieurs projections**.

### b) Support des projections multi-agrégats

Afin de permettre à une vue d’être alimentée par plusieurs agrégats, il est pertinent :
*   d’introduire une couche de normalisation des événements,
*   de définir des projections capables de consommer plusieurs types d’événements,
*   d’adopter un modèle de projection **idempotent**.

### c) Scalabilité et distribution des projections

Pour améliorer la scalabilité :
*   les projections peuvent être traitées de manière **asynchrone**,
*   les gestionnaires peuvent être répartis sur plusieurs instances,
*   des mécanismes de partitionnement ou de routage peuvent être introduits.

## 3. Évolution des projections et initialisation des nouvelles vues

Dans l’implémentation actuelle, l’outbox est alimentée uniquement à partir des modifications futures des agrégats.

### Problème identifié

Dans le cas où une nouvelle vue projetée serait ajoutée à partir d’un agrégat existant, cette vue ne disposerait pas des données historiques nécessaires à son initialisation correcte. Les événements passés ne seraient pas rejoués, entraînant une vue vide ou incohérente.

### Solution proposée

Pour garantir l’initialisation correcte d’une nouvelle vue, plusieurs approches sont possibles :
1.  **Rejeu des événements (Event Replay)** : Rejouer les événements historiques à partir du journal pour reconstruire la vue depuis l’origine.
2.  **Snapshot initial** : Créer un snapshot de l’état courant des agrégats, suivi de la consommation des événements futurs.
3.  **Migration contrôlée** : Combiner extraction des données existantes et activation progressive de la nouvelle projection.
