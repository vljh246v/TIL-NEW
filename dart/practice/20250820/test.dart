void main() {
  dynamic name = '소녀시대';
  print(name);
  dynamic number = 1;
  print(number);

  var name2 = '티파니';
  print(name2);

  print(name.runtimeType);
  print(number.runtimeType);

  name = 2;
  print(name.runtimeType);
}