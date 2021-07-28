with open('personal.csv', 'r') as file :
  filedata = file.read()

# Replace the target string
filedata = filedata.replace('，', ', ')

# Write the file out again
with open('personal.csv', 'w') as file:
  file.write(filedata)
