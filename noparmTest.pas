program noparmTest (input, output);
 var a, b, c : integer;
  function two: result integer;
    var n : integer;
    begin
      c := a + b;
      two := -c
    end
  begin
    a := 1;
    b := 2;
    c := a+b+two;
    write(c)
  end.