void main() {
  final String name = '임재현';
  print(name);
  // name = '임재현1'; // This line will cause an error because 'name' is final

  const String name2 = '티파니';
  print(name2);

  // name2 = '티파니2'; // This line will cause an error because 'name2' is const
}