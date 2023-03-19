# TinySRVNT

## A small D&D character manager
A port of a personal Discord bot project (similarly unfinished) of the same name.

Expected functionality (extended functionality in brackets):
- Ability to read character sheets as defined by a custom file format
- Ability to write character sheets as defined by a custom file format
- Ability to create character sheets using standard 5th Edition game rules


- (Ability to represent a current combat encounter with multiple characters)
- (Ability to allow users to make all changes necessary to simulate a combat encounter)
- (Ability for users to create creatures and effects necessary to simulate a combat encounter)

## User Stories:
As a user, I want to be able to:

- Add a Class to a new Character
- Set the Race of a new Character
- Set the Background of a new Character
- Add a Class to an existing Character
- Change the Name of an existing Character
- Change the Race of an existing Character
- Change the Background of an existing Character
- Add levels to an existing Character
- Add Items to a Character's inventory


- Save my progress upon exiting the application
- Load progress from backup files upon entering the application at a later time

## Instructions for Grader

- You can generate the first required event related to adding Xs to a Y by selecting a save to load, and 
with a character registered and loaded into the system, clicking the "Add Item" button in the center of 
the screen. This will add the item selected from the combo box below it to the character's equipped items.
- You can generate the second required event related to adding Xs to a Y by selecting a save to load, and
with a character registered and loaded into the system, clicking the "Add Feature" button in the center of
the screen. This will add the feature selected from the combo box below it to the character.
- You can locate my visual component by glancing at the top of the screen! The title bar is an image.
- You can save the state of my application by clicking the header tab labelled "File", then clicking the menu option labelled "Save Workspace Image"
- You can reload the state of my application by clicking the header tab labelled "File", then clicking the menu option labelled "Load Workspace Image"

## Phase 4: Task 2
Sample Event Log: (Simultaneous event timestamps removed for brevity and clustering)

Fri Dec 02 11:26:44 PST 2022\
New character created: ty\
New item created: hot dog\
Item hot dog weight set to 0\
Item hot dog price set to 0\
New Score feature created: Test Feature\
Fri Dec 02 11:26:57 PST 2022\
New item created: watermelon\
Item watermelon price set to 4\
Item watermelon weight set to 12\
Fri Dec 02 11:27:13 PST 2022\
New item created: The Horse\
Item The Horse price set to -10\
Item The Horse weight set to 50\
Fri Dec 02 11:27:18 PST 2022\
Item watermelon added to character ty's equipment\
Fri Dec 02 11:27:20 PST 2022\
Item The Horse added to character ty's equipment\
Fri Dec 02 11:27:22 PST 2022\
Feature Test Feature added to character ty\
Fri Dec 02 11:27:23 PST 2022\
Feature Test Feature added to character ty\
Fri Dec 02 11:27:24 PST 2022\
Feature Test Feature added to character ty\
Fri Dec 02 11:27:34 PST 2022\
New character created: Test Character\
New Score feature created: Test Score Feature\
Feature Test Score Feature added to character Test Character\
New description-only feature created: Test Feature\
Feature Test Feature added to character Test Character\
New item created: HotDog\
Item HotDog weight set to 0\
Item HotDog price set to 0\
Item HotDog added to character Test Character's equipment\
New item created: HotDog\
Item HotDog weight set to 0\
Item HotDog price set to 0\
New item created: HotDog\
Item HotDog weight set to 0\
Item HotDog price set to 0\
New item created: HotDog\
Item HotDog weight set to 0\
Item HotDog price set to 0\
New description-only feature created: Test Feature\
New Score feature created: Test Score Feature\
Fri Dec 02 11:27:37 PST 2022\
Item HotDog added to character Test Character's equipment\
Fri Dec 02 11:27:38 PST 2022\
Item HotDog added to character Test Character's equipment\
Fri Dec 02 11:27:44 PST 2022\
New item created: Not A Word\
Item Not A Word price set to 0\
Item Not A Word weight set to 0\
Fri Dec 02 11:27:45 PST 2022\
Item Not A Word added to character Test Character's equipment\
Fri Dec 02 11:27:48 PST 2022\
Feature Test Score Feature added to character Test Character\
Fri Dec 02 11:27:49 PST 2022\
Feature Test Score Feature added to character Test Character\

## Phase 4: Task 3
Refactoring:

- `ScoreType` and `StatType` have duplicate methods for application of modifiers; replace with inherited methods 
from a singular superclass
- "`ListPanel`-type" classes in gui need to be consolidated into one arbitrary-type `ListPanel`
class, with `ScrollPane` functionality built into the `ListPanel` instead of a externally-defined
wrapper panel
- Use and implement exception-based handling more
- Consolidate `ScoreType` and `StatType` into a single enum with metadata
- Use enum metadata more for easier handling
- Segment `Feature` class into multiple subclasses either extending a superclass or extending a `Feature`
interface for easier handling (possibly with a composite design pattern for `MULTI` type)
- Swap some `List`s with `Set`s for more streamlined handling (`Feature`s in `Character`, for instance)
- Perhaps decompose and delegate stat and score handling and updating from `Character` to another class?
- Replace the double "Class and Level" `List`+`Map` system in `Character` with a single `Map` that contains
the class index with the class name key