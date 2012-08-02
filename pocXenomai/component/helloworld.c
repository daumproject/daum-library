#include <stdio.h>
#include <sys/mman.h>
#include <native/task.h>

static void task(void *arg)
{
  /* Here, we are in primary mode... */

  printf("Hello world!\n");

  /* Outch... printf accesses a Linux ressource so we are now
     in secondary mode */
}

int main (void)
{
  RT_TASK task_desc;
  
  /* disable memory swap */
  mlockall( MCL_CURRENT | MCL_FUTURE );
  
  if (rt_task_spawn( &task_desc,  /* task descriptor */
                     "my task",   /* name */
                      0           /* 0 = default stack size */,
              99          /* priority */,
              T_JOINABLE, /* needed to call rt_task_join after */
              &task,      /* entry point (function pointer) */
              NULL        /* function argument */ )!=0)
  {
    printf("rt_task_spawn error\n");
    return 1;
  }

  /* wait for task function termination */
  rt_task_join(&task_desc);
  
  return 0;
}
