CHARACTERS = []
TOWNS = []

class Character:

    def __init__(self, name):
        if name in CHARACTERS:
            raise ValueError('Character with given name already exists.')
        self.name = name
        CHARACTERS.append(name)


class Town:
    def __init__(self, name, neighbors):
        # Checking if town already exists with the same name and if the list of neighbor is valid
        if name in TOWNS:
            raise ValueError('Town with given name already exists.')
        elif name in neighbors:
            raise ValueError('Town cannot have itself as a neighbor.')

        self.name = name
        TOWNS.append(name)
        self.neighbors = neighbors
        self.character = None
        self.update_neighbors()

    def add_neighbor(self, town):
        ''' Adds given town to this town's list of neighbors if not already present. '''
        if town not in self.neighbors:
            self.neighbors.append(town)

    def update_neighbors(self):
        ''' Adds self to neighbors' list of neighbors if necessary. '''
        for neighbor in self.neighbors:
            neighbor.add_neighbor(self)

    def set_character(self, character):
        ''' Sets the character for the town, if there is no character in the town already. '''
        if not self.character:
            self.character = character

    def check_paths(self, character):
        if self.character:
            return self.character.name == character.name
        else:
            result = False
            for neighbor in self.neighbors:
                result = result or neighbor.check_paths(character)
            return result

def create_character(name):
    '''Creates and returns a Character with the given name'''
    return Character(name)

def create_town(name, neighbors):
    '''Creates and returns a Town with the given name and list of given neighboring towns. '''
    return Town(name, neighbors)

def add_character(character, town):
    ''' Set the given Town's character to the given Character. '''
    town.set_character(character)

def path_exists(character, town):
    '''Returns true if the given Character can reach the given Town without running into any other Characters. '''
    return town.check_paths(character)

def main():
    a = create_town('A', [])
    b = create_town('B', [a])
    c = create_town('C', [b])
    d = create_town('D', [a, c])

    z = create_character('Z')
    y = create_character('Y')

    add_character(z, a)
    add_character(y, b)

    print("z->d: True")
    print(path_exists(z, d))
    print("z->c: True")
    print(path_exists(z, c))
    print("z->b: False")
    print(path_exists(z, b))
    print("z->a: True")
    print(path_exists(z, a))

    print("y->d: True")
    print(path_exists(y, d))
    print("y->c: True")
    print(path_exists(y, c))
    print("y->b: True")
    print(path_exists(y, b))
    print("y->a: False")
    print(path_exists(y, a))

if __name__ == '__main__':
    main()
