import os

proj_root_dir = os.getcwd()
optimized_ir_file = os.path.join(proj_root_dir, 'out.ir')
test_dirs = [os.path.join(proj_root_dir, 'example'), os.path.join(proj_root_dir, 'public_test_cases/sqrt'), os.path.join(proj_root_dir, 'public_test_cases/quicksort')]
test_ir_files = [os.path.join(test_dirs[0], 'example.ir'), os.path.join(test_dirs[1], 'sqrt.ir'), os.path.join(test_dirs[2], 'quicksort.ir')]


optimize_cmd = './run.sh {} > /dev/null'					# format(ir_file)
run_test_cmd = 'java -cp ./build IRInterpreter {} < {}' 	# format(ir_file, input_file)

if __name__ == "__main__":
	for i in range(3):
		os.system(optimize_cmd.format(test_ir_files[i]))	# generate optimized out.ir
		for test in os.listdir(test_dirs[i]):
			if test[-3:] == '.in':
				test_in = os.path.join(test_dirs[i], test)
				test_out = test_in[:-3]+'.out'
				# print(f"test_in = {test_in}\ttest_out = {test_out}")
				print(f'[ TEST {test[:-2]} ] --> {test_dirs[i]}')
				print('==--=='*5)
				print('Provided:\n')
				os.system(run_test_cmd.format(test_ir_files[i], test_in))
				# print(f"{run_test_cmd.format(test_ir_files[i], test_in)}")
				print()
				os.system(f'cat {test_out}')
				print('==--====--==')
				print('Optimized:\n')
				os.system(run_test_cmd.format(optimized_ir_file, test_in))
				# print(f"{run_test_cmd.format(optimized_ir_file, test_in)}")
				print('\n==--====--==\n\n')
				print('==--=='*5)

