import pygame
import random
from pygame import *
mixer.init()
pygame.init()
width = 640
height = 480
block_size = 10
screen = pygame.display.set_mode((width,height),pygame.FULLSCREEN)
pygame.display.set_caption('Snake Game')
pygame.display.update()
clock = pygame.time.Clock()
diff = block_size
font = pygame.font.SysFont("Tahoma",25)
def snake(snakelist):
   for xy in snakelist:
      pygame.draw.rect(screen,(0,255,0),[xy[0],xy[1],block_size,block_size])
   xy = snakelist[len(snakelist)-1]
   pygame.draw.rect(screen,(255,34,24),[xy[0],xy[1],block_size,block_size])
def msg(pos,m,color):
   text = font.render(m,True,color)
   screen.blit(text,[pos[0],pos[1]])
def main():
   snakelist = []
   snakelen = 1
   f = 0
   FPS = block_size
   isExit = False
   x = width/2
   y = height/2
   flag_x = -1
   flag_y = -1
   gameOver = False
   count = 0
   randx = random.randrange(0,width-block_size,block_size)
   randy = random.randrange(0,height-block_size,block_size)
   while not isExit:
      milli = clock.tick(FPS)
      if(gameOver == True):
         msg([(width/2)-160,(height/2)-100],'''Game Over!! Your Score is :'''+str(count),(0,0,255))
         msg([(width/2)-240,(height/2-50)],'''Press Y to Play Again or N to leave the game''',(0,0,255))
         pygame.display.update()
         pygame.mixer.music.load("smb_mariodie.ogg")
         pygame.mixer.music.play()
      while gameOver == True:
         for e in pygame.event.get():
            if e.type == pygame.KEYDOWN:
               if e.key == pygame.K_y:
                  main()
               elif e.key == pygame.K_n:
                  isExit = True
                  gameOver = False
      for event in pygame.event.get():
         if event.type == pygame.QUIT:
            isExit = True
         elif event.type == pygame.KEYDOWN:
            if event.key == pygame.K_ESCAPE:
               isExit = True
            if event.key == pygame.K_LEFT:
               if(flag_y!=-1):
                  flag_x = 0
                  flag_y = -1
            if event.key == pygame.K_RIGHT:
               if(flag_x!=0):
                  flag_x = 1
                  flag_y = -1
            if event.key == pygame.K_UP:
               if(flag_y!=1):
                  flag_y = 0
                  flag_x = -1
            if event.key == pygame.K_DOWN:
               if(flag_y!=0):
                  flag_y = 1
                  flag_x = -1
      pygame.display.set_caption(str(x)+" "+str(y))
      pygame.display.flip()
      if x>width+int(snakelen):
         x = -block_size
      elif x<0:
         x = width
      if y>height+int(snakelen):
         y = -block_size
      elif y<0:
         y = height
      if flag_x == 0:
         x -= diff
      elif flag_x == 1:
         x += diff
      elif flag_y == 0:
         y -= diff
      elif flag_y == 1:
         y += diff
      newlist = snakelist[1:]
      for xy in newlist:
         if x == xy[0] and y == xy[1]:
            gameOver = True
            f = 1
            snakt = []
            snakt.append(x)
            snakt.append(y)
            snakelist.append(snakt)
            snake(snakelist)
      if f!=1:
         if x == randx and y == randy:
            count += 1
            snakelen += 1
            if snakelen%5 == 0:
               FPS += int(snakelen/5)
            mixer.music.load("smw_fireball.ogg")
            lis = []
            lis.append(x)
            lis.append(y)
            snakelist.append(lis)
            randx = random.randrange(0,width-block_size,block_size)
            randy = random.randrange(0,height-block_size,block_size)
            print(str(randx)+" "+str(randy))
            pygame.draw.rect(screen,(0,0,0),(randx,randy,block_size,block_size))
            mixer.music.play()
            pygame.display.flip()
         else:
            screen.fill((255,255,0))
            snakeHead = []
            snakeHead.append(x)
            snakeHead.append(y)
            snakelist.append(snakeHead)
            if(len(snakelist)>snakelen):
               del snakelist[0]
            snake(snakelist)
            pygame.draw.rect(screen,(0,0,0),(randx,randy,block_size,block_size))
            pygame.display.flip()
   pygame.quit()
main()
