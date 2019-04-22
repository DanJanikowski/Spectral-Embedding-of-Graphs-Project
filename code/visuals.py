import matplotlib.pyplot as plt
import random
import numpy as np
from mpl_toolkits.mplot3d import Axes3D

n = 75
scale = 4.0
pert = 0.1
x1 = scale*np.random.rand(n)
y1 = pert*np.random.rand(n)-(pert/2.0)
for i in range(len(y1)):
    y1[i] += -((x1[i] - scale/2.0)**2.0)/(scale*scale)+0.5
x1 /= scale

x2 = scale*np.random.rand(n)
y2 = pert*np.random.rand(n)-(pert/2.0)
for i in range(len(y2)):
    y2[i] += ((x2[i] - scale/2.0)**2.0)/(scale*scale)
x2 /= scale
x2 += 0.5
x = np.ndarray.tolist(x1)
y = np.ndarray.tolist(y1)
for i in x2:
    x.append(i)
for i in y2:
    y.append(i)

thresholdDist = 0.4
A = np.identity(len(x))
for i in range(len(A)):
    for j in range(len(A[0])):
        dist = (x[i]-x[j])**2.0+(y[i]-y[j])**2.0
        if dist < thresholdDist**2:
            A[i][j] = dist
        else:
            A[i][j] = 0

D = np.identity(len(x))
for i in range(len(D)):
    for j in range(len(D[0])):
        if i == j:
            D[i][j] = 0
            for k in A[i]:
                D[i][j] += k

L = A
eigval, eigvec = np.linalg.eig(L)
np.where(eigval == np.partition(eigval, 1)[1])

y_spec =eigvec[:,1].copy()
y_spec[y_spec < 0] = 0
y_spec[y_spec > 0] = 1

# sorted = [[a, b] for a, b in zip(eigvals, eigvecs)]
# sorted.sort(key=lambda x: x[0])
# eVals = np.asarray([x[0] for x in sorted])
# eVecs = np.asarray([x[1] for x in sorted])
# print(eVals)
# second = np.matrix.transpose(eVecs)[2]

for i in range(len(x)):
    c = (0, 1, 0)
    if y_spec[i] == 1:
        c = (0, 0, 1)
    plt.scatter(x[i], y[i], color=c)
plt.show()